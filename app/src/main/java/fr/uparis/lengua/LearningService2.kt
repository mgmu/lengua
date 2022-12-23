package fr.uparis.lengua

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData

class LearningService2 : LifecycleService() { /* for observers */
    private val TAG = "LOGLENGUASERVICE2"
    private var hasToStop = false /* Indicates if this service has to stop. */
    private var delayBetweenWakesInMs = 15000 /* Time between display of notifications = 15 seconds. */
    private var notificationsToDisplay = 10 /* Number of notifications to display each batch. */
    private val dao by lazy {(application as TranslationApplication).database.iDao()}
    private lateinit var allWordsInDB: LiveData<List<Word>> /* All the words in the DB. */

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
        Log.d(TAG, "onCreate()")
        allWordsInDB = dao.loadAllWords()
        createNotificationChannel()
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

        Log.d(TAG, "onStartCommand()")

        allWordsInDB.observe(this) {
            Log.d("LOGLENGUASERVICE2", "CHANGE OF WORDS IN DB")
            val lastDisplayTime =
                sharedPreferences.getLong(getString(R.string.last_display_time_key), -1)
            if (lastDisplayTime == -1L
                || lastDisplayTime + delayBetweenWakesInMs <= System.currentTimeMillis()) {
                    Log.d(TAG, "Never displayed notifications before or triggered by other service")
                    displayNotifications()
                    updateLastDisplayTime(System.currentTimeMillis())
            }
        }

        /* Wake up service in DELAY milliseconds if it does not have to stop. */
        if (!hasToStop) {
            val triggerTime = (System.currentTimeMillis() + delayBetweenWakesInMs)
            val alarmIntent = Intent(this, LearningService2::class.java)
            val pending = PendingIntent.getService(this, 1, alarmIntent, pendingFlag)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pending)
        }

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
        Log.d(TAG, "displayNotifications()")
        val words = drawRandomWords() // Draw at most notificationsToDisplay words from loaded words
        val notifications = mutableListOf<Notification>() // Will hold the notifications with words
        for (id in words.indices)
            notifications.add(createNotificationFromWord(words[id], id))
        for (id in notifications.indices)
            notificationManager.notify(id, notifications[id]) // Display each notification
    }

    private fun createSwipeIntent(word: Word, id: Int): Intent {

        // the intent to send when the notification is swiped
        val swipeIntent = Intent(this, NotificationDismissReceiver::class.java)
        val swipeIntentExtras = Bundle().apply {
            putString(getString(R.string.word_word_key), word.word)
            putString(getString(R.string.word_src_language_key), word.sourceLanguage)
            putString(getString(R.string.word_dest_language_key), word.sourceLanguage)
            putString(getString(R.string.word_link_key), word.link)
            putInt(getString(R.string.notification_id_key), id)
        }
        swipeIntent.putExtras(swipeIntentExtras)
        Log.d(TAG, "Created swipe intent for [$word] of id [$id]")
        return swipeIntent
    }

    /**
     * Creates a Notification with the given word and returns it.
     * */
    private fun createNotificationFromWord(word: Word, id: Int): Notification {
        val swipeIntent = createSwipeIntent(word, id)
        val pendingSwipeIntent =
            PendingIntent.getBroadcast(this, id, swipeIntent, pendingFlag)

        val id = getString(R.string.channel_id)
        Log.d(TAG, "Created notification for [$word] of id [$id]")
        return NotificationCompat.Builder(this, id)
            .setContentTitle(word.word)
            .setContentText("${word.sourceLanguage} -> ${word.destinationLanguage}")
            .setSmallIcon(R.drawable.github)
            .setDeleteIntent(pendingSwipeIntent)
            .build()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d(TAG, "onBind()")
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }

    /**
     * Draws notificationsToDisplay different random words from the loaded words and returns
     * them in a list. If there are no loaded words, returns an empty list.
     * */
    private fun drawRandomWords(): List<Word> {
        if (allWordsInDB.value != null) { // If words have not been retrieved yet, do nothing
            var list = mutableListOf<Word>()

            if (notificationsToDisplay > allWordsInDB.value!!.size) // Adapt to number of elements in DB
                notificationsToDisplay = allWordsInDB.value!!.size

            var missing = notificationsToDisplay
            while (list.size != notificationsToDisplay) {
                for (i in 0 until missing) // add missing random words
                    list.add(allWordsInDB.value!![(0 until allWordsInDB.value!!.size).random()])
                list = list.distinct().toMutableList() // remove duplicates
                missing = notificationsToDisplay - list.size // update missing
            }

            return list
        }
        return emptyList()
    }
}