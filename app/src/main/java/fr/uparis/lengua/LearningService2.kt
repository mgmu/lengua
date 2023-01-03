package fr.uparis.lengua

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import java.lang.Integer.max
import kotlin.concurrent.thread

class LearningService2 : LifecycleService() { /* for observers */
    private var hasToStop = false /* Indicates if this service has to stop. */
    private var delayBetweenWakesInMs = 20000 /* Time between display of notifications = 20 seconds. */
    private var notificationsToDisplay:Int = 10 /* Number of notifications to display each batch. */
    private val dao by lazy {(application as TranslationApplication).database.iDao()}
    private lateinit var allWordsInDB: LiveData<List<Word>> /* All the words in the DB. */
    private var notifications: MutableList<Notification>? = null
    private var wordsNotified: MutableList<Word>? = null
    private val _limitSwipe = 3

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private val alarmManager by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val pendingFlag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            PendingIntent.FLAG_UPDATE_CURRENT

    private val sharedPreferences by lazy {
        applicationContext
            .getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        allWordsInDB = dao.loadAllWords()
        createNotificationChannel()
        notificationsToDisplay = sharedPreferences.getInt(R.string.words_per_lesson.toString(),10)
    }

    /**
     * Creates the notification channel for this App
     * */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val id = getString(R.string.channel_id)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
                .apply { description = getString(R.string.channel_description) }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        delayBetweenWakesInMs = sharedPreferences.getInt(getString(R.string.recap_frequency),delayBetweenWakesInMs)
        notificationsToDisplay = sharedPreferences.getInt(getString(R.string.words_per_lesson),notificationsToDisplay)
        // Update word in db and displayed notification
        if (intent != null && intent.action == "update" && allWordsInDB.value != null) {
            if (intent.extras != null && wordsNotified != null && notifications != null) {
                val notificationID = intent.extras!!.getInt(getString(R.string.notification_id_key))
                val notifiedWord = wordsNotified!![notificationID]
                notifiedWord.swiped++
                thread { dao.updateWord(notifiedWord) }
                if (allWordsInDB.value!!.isEmpty() || allWordsInDB.value!!.size <= notificationsToDisplay)
                    return START_NOT_STICKY
                var newWord = notifiedWord
                while (wordsNotified!!.contains(newWord) || newWord.swiped >= _limitSwipe)
                    newWord = allWordsInDB.value!![(allWordsInDB.value!!.indices).random()]
                val newNotification = createNotificationFromWord(newWord, notificationID)
                wordsNotified!![notificationID] = newWord
                notifications!![notificationID] = newNotification
                notificationManager.notify(notificationID, newNotification)
            }
            return START_NOT_STICKY
        }

        allWordsInDB.observe(this) {
            val lastDisplayTime =
                sharedPreferences.getLong(getString(R.string.last_display_time_key), -1)
            if (lastDisplayTime == -1L
                || lastDisplayTime + delayBetweenWakesInMs <= System.currentTimeMillis()) {
                    displayNotifications()
                    updateLastDisplayTime(System.currentTimeMillis())
            }
        }

        /* Wake up service in DELAY milliseconds if it does not have to stop. */
        if (!hasToStop) {
            val triggerTime = System.currentTimeMillis() + delayBetweenWakesInMs
            val alarmIntent = Intent(this, LearningService2::class.java)
            val pending = PendingIntent.getService(this, 1, alarmIntent, pendingFlag)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pending)
        }

        /* Wake up service in DELAY milliseconds. */
        val triggerTime = (System.currentTimeMillis() + delayBetweenWakesInMs)
        val alarmIntent = Intent(this, LearningService2::class.java)
        val pending = PendingIntent.getService(this,1, alarmIntent, pendingFlag)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pending)
        return START_NOT_STICKY
    }

    private fun updateLastDisplayTime(time: Long) {
        with(sharedPreferences.edit()) {
            putLong(getString(R.string.last_display_time_key), time)
            apply()
        }
    }

    /**
     * Draws at most notificationsToDisplay random different words from the loaded words
     * and creates a notification for each of them which is finally notified to the notification
     * manager.
     * */
    private fun displayNotifications() {
        wordsNotified = drawRandomWords() // Draw at most notificationsToDisplay words from loaded words

        if (notifications == null) // first start of service after a destruction
            notifications = mutableListOf()
        else // remove old notifications as new ones are coming
            notifications!!.clear()

        for (id in wordsNotified!!.indices)
            notifications!!.add(createNotificationFromWord(wordsNotified!![id], id))

        for (id in notifications!!.indices)
            notificationManager.notify(id, notifications!![id])
    }

    private fun createSwipeIntent(id: Int): Intent {
        // the intent to send when the notification is swiped
        val swipeIntent = Intent(this, NotificationDismissReceiver::class.java)
        val swipeIntentExtras = Bundle().apply { putInt(getString(R.string.notification_id_key), id) }
        swipeIntent.putExtras(swipeIntentExtras)
        return swipeIntent
    }

    private fun createWebIntent(word: Word): Intent {
        val uri = Uri.parse(word.link)
        return Intent(Intent.ACTION_VIEW, uri)
    }

    /**
     * Creates a Notification with the given word and returns it.
     * */
    private fun createNotificationFromWord(word: Word, id: Int): Notification {
        val swipeIntent = createSwipeIntent(id)
        val pendingSwipeIntent =
            PendingIntent.getBroadcast(this, id, swipeIntent, pendingFlag)

        val webIntent = createWebIntent(word)
        val pendingWebIntent =
            PendingIntent.getActivity(this, id, webIntent, pendingFlag)

        val chanId = getString(R.string.channel_id)
        return NotificationCompat.Builder(this, chanId)
            .setContentTitle(word.word)
            .setContentIntent(pendingWebIntent)
            .setContentText("${word.sourceLanguage} -> ${word.destinationLanguage}")
            .setSmallIcon(R.drawable.github)
            .setDeleteIntent(pendingSwipeIntent)
            .build()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        TODO("Return the communication channel to the service.")
    }

    /**
     * Draws notificationsToDisplay different random words from the loaded words and returns
     * them in a list. If there are no loaded words, returns an empty list.
     * */
    private fun drawRandomWords(): MutableList<Word> {
        if (allWordsInDB.value != null) { // If words have not been retrieved yet, do nothing
            var list = mutableListOf<Word>()

            // Adapt to number of elements in DB
            if (notificationsToDisplay > allWordsInDB.value!!.size)
                notificationsToDisplay = allWordsInDB.value!!.size

            // count words that can't be displayed anymore
            var count = 0
            for (word in allWordsInDB.value!!)
                if (word.swiped >= 3) count++

            // adapt notificationsToDisplay to number of elements that can't be displayed anymore
            notificationsToDisplay -= count

            var missing = notificationsToDisplay
            while (list.size != notificationsToDisplay) {
                for (i in 0 until missing) // add missing random words
                    list.add(allWordsInDB.value!![(0 until allWordsInDB.value!!.size).random()])
                list = list.distinct().toMutableList() // remove duplicates
                missing = notificationsToDisplay - list.size // update missing
            }

            return list
        }
        return emptyList<Word>().toMutableList()
    }
}
