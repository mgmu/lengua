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
import kotlin.concurrent.thread

class LearningService2 : LifecycleService() { /* for observers */
    private val _tag = "Log of learning service"
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
        Log.d(_tag, "onCreate()")
        allWordsInDB = dao.loadAllWords()
        createNotificationChannel()
        notificationsToDisplay = sharedPreferences.getInt(R.string.words_per_lesson.toString(),10)
        Log.d(_tag,"Notification to display : $notificationsToDisplay")
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

        Log.d(_tag, "LearningService2::onStartCommand()")

        delayBetweenWakesInMs = sharedPreferences.getInt(getString(R.string.recap_frequency),delayBetweenWakesInMs)
        notificationsToDisplay = sharedPreferences.getInt(getString(R.string.words_per_lesson),notificationsToDisplay)
        Log.d(_tag,"Notification to display : $notificationsToDisplay")
        Log.d(_tag,"Delay between wake ups : $delayBetweenWakesInMs")
        // Update word in db and displayed notification
        if (intent != null && intent.action == "update" && allWordsInDB.value != null) {
            Log.d(_tag, "UPDATE ACTION")
            if (intent.extras != null && wordsNotified != null && notifications != null) {
                val notificationID = intent.extras!!.getInt(getString(R.string.notification_id_key))
                Log.d(_tag, "recovered notification")
                val notifiedWord = wordsNotified!![notificationID]
                Log.d(_tag, "recovered notifiedWord")
                if (allWordsInDB.value!!.isEmpty() || allWordsInDB.value!!.size <= notificationsToDisplay)
                    return START_NOT_STICKY
                var newWord = notifiedWord
                while (wordsNotified!!.contains(newWord) || newWord.swiped >= _limitSwipe) {
                    Log.d(_tag, "drawing random word")
                    newWord = allWordsInDB.value!![(allWordsInDB.value!!.indices).random()]
                }
                Log.d(_tag, "drawn new random word")
                val newNotification = createNotificationFromWord(newWord, notificationID)
                Log.d(_tag, "created new notification")
                wordsNotified!![notificationID] = newWord
                Log.d(_tag, "assigned new word in notified words")
                notifications!![notificationID] = newNotification
                Log.d(_tag, "assigned new notification in displayed notifications")
                notificationManager.notify(notificationID, newNotification)
                Log.d(_tag, "notified new notification")
                notifiedWord.swiped++
                Log.d(_tag, "about to start thread for word update")
                thread { dao.updateWord(notifiedWord) }
                Log.d(_tag, "started thread for word update")
                Log.d(_tag, "end of UPDATE ACTION")
            }
            return START_NOT_STICKY
        }

        Log.d("crash_swipe", "intent != null ${intent != null}")
        Log.d("crash_swipe", "intent.action == \"update\" ${intent?.action == "update"}")
        Log.d("crash_swipe", "allWordsInDB.value != null ${allWordsInDB.value != null}")

        allWordsInDB.observe(this) {
            Log.d(_tag, "LearningService2::CHANGE OF WORDS IN DB")
            val lastDisplayTime =
                sharedPreferences.getLong(getString(R.string.last_display_time_key), -1)
            if (lastDisplayTime == -1L
                || lastDisplayTime + delayBetweenWakesInMs <= System.currentTimeMillis()) {
                    Log.d(_tag, "LearningService2::Never displayed notifications before or triggered by other service")
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
        Log.d(_tag, "LearningService2::displayNotifications()")
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
        Log.d(_tag, "LearningService2::Created swipe intent for id [$id]")
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
        Log.d(_tag, "LearningService2::Created notification for [$word] of id [$id]")
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
        Log.d(_tag, "LearningService2::onBind()")
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        Log.d(_tag, "LearningService2::onDestroy()")
        super.onDestroy()
    }

    /**
     * Draws notificationsToDisplay different random words from the loaded words and returns
     * them in a list. If there are no loaded words, returns an empty list.
     * */
    private fun drawRandomWords(): MutableList<Word> {
        if (allWordsInDB.value != null) { // If words have not been retrieved yet, do nothing
            var list = mutableListOf<Word>()

            if (notificationsToDisplay > allWordsInDB.value!!.size) // Adapt to number of elements in DB
                notificationsToDisplay = allWordsInDB.value!!.size
            Log.d(_tag,"number of notifs in draw randomWord : $notificationsToDisplay")
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
