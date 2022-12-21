package fr.uparis.lengua

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData

class LearningService2 : LifecycleService() { /* for observers */
    private val TAG = "LOGLENGUASERVICE2"
    private var hasToStop = false /* Indicates if this service has to stop. */
    private var delayBetweenWakesInMs = 60000 /* Time between display of notifications = 1 minute. */
    private var notificationsToDisplay = 10 /* Number of notifications to display each batch. */
    private val dao by lazy {(application as TranslationApplication).database.iDao()}
    private lateinit var allWordsInDB: LiveData<List<Word>> /* All the words in the DB. */
    private var notificationsDisplayed = false /* Indicates if the notification has been displayed. */
    private val CHANNEL_ID = "Lengua Notification Channel ID"
    private val CHANNEL_NAME = "Lengua Notification Channel"
    private val CHANNEL_DESCRIPTION = "Lengua Notification Channel Description"

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
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, // Nom du channel
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = CHANNEL_DESCRIPTION }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d(TAG, "onStartCommand()")

        /* Load NOTIFICATIONSTODISPLAY*/
        allWordsInDB.observe(this) {
            if (!notificationsDisplayed)
                displayNotifications()
        }

        /* Wake up service in DELAY milliseconds. */
        val triggerTime = (System.currentTimeMillis() + delayBetweenWakesInMs).toLong()
        val alarmIntent = Intent(this, LearningService2::class.java)
        val pending = PendingIntent.getService(this,1, alarmIntent, pendingFlag)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pending)

        return START_NOT_STICKY
    }

    /**
     * Draws at most notificationsToDisplay random different words from the loaded words
     * and creates a notification for each of them which is finally notified to the notification
     * manager.
     * */
    private fun displayNotifications() {
        Log.d(TAG, "displayNotifications()")
        notificationsDisplayed = true
        val words = drawRandomWords() // Draw at most notificationsToDisplay words from loaded words
        val notifications = mutableListOf<Notification>() // Will hold the notifications with words
        for (word in words)
            notifications.add(createNotificationFromWord(word))
        for (id in 0 until notifications.size) {
            notificationManager.notify(id, notifications[id]) // Display each notification
        }
    }

    /**
     * Creates a Notification from the word and returns it.
     * */
    private fun createNotificationFromWord(word: Word): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(word.word)
            .setContentText("${word.sourceLanguage} -> ${word.destinationLanguage}")
            .setSmallIcon(R.drawable.github)
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
                    list.add(allWordsInDB.value!![(0 until notificationsToDisplay).random()])
                list = list.distinct().toMutableList() // remove duplicates
                missing = notificationsToDisplay - list.size // update missing
            }

            return list
        }
        return emptyList()
    }
}