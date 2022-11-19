package fr.uparis.lengua

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.random.Random

/**
 * This service allows the user to review words by using notifications.
 */
class LearningService : Service() {

    /**
     * Id for the notification channel
     */
    private val CHANNEL_ID = "Learning channel"

    /**
     * Automatic numbering of the notifications.
     */
    private var currentNotificationID = 0

    /**
     * Getting sharedprefrences to know how many notification we should send.
     */
    private val sharedPreferences by lazy {
        getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
    }

    /**
     * Stores the the words of the database
     */
    private lateinit var words : List<Word>

    /**
     * Getting notification manager
     */
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    /**
     * Deciding which flag to use depending on sdk version
     */
    private val pendingFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        PendingIntent.FLAG_IMMUTABLE
    else
        PendingIntent.FLAG_UPDATE_CURRENT

    private val dao: IDao by lazy {
        (application as TranslationApplication).database.iDao()
    }

    /**
     * Notification channel creation before starting service
     */
    override fun onCreate() {
        super.onCreate()
        Log.d("logLENGUA","onCreateService")
        createNotificationChannel()
        fillWords()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("logLENGUA","onstartCommand")
        var remainingPlace =  sharedPreferences.getInt("nbNotifs",0)
        sendNotifications(remainingPlace)
        triggerAlarm()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun fillWords(): Unit{
//        words = dao.loadAllWords().value!!
    }

    /**
     * Draws a random word from the database
     * @return the drawn word
     */
    private fun getRandomWord(): Word{
        return words[Random.nextInt(words.size)]
    }

    /**
     * Creates a notification channel...
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "private channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "private channel" }

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds a notification containing one word from the database.
     * When the user tap on the notification the translation webpage
     * of the given word will be displayed
     * @return a notification...
     */
    private fun createNotification(): Notification {

        val word = getRandomWord()
        val notif = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(word.toString())
            .setContentText("Tap to see translation")
            .setSmallIcon(androidx.core.R.drawable.notification_bg_low_normal) // should find a icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
//            .setDeleteIntent(PendingIntent)
            .addAction(
              R.drawable.ic_launcher_background ,"More",
                PendingIntent.getActivity(this,0, launchBrowser(word.link),
                    pendingFlag)
            )
            .build()

        currentNotificationID++
        return notif
    }

    /**
     * Sends notifications used to see the translation of a given word
     * @param nbOfNotifications will be sent
     */
    private fun sendNotifications(nbOfNotifications:Int): Unit {
        for (i in 0 until nbOfNotifications)
            notificationManager.notify(currentNotificationID, createNotification())
    }

    /**
     * Searches the given address on the web
     * @param address to look for
     * @return the intent that launches the browser
     */
    private fun launchBrowser(address:String):Intent {
        val url = Uri.parse(address)
        val browserIntent = Intent(Intent.ACTION_VIEW,url)
        // The notification is used so we free the place for an other notification in the next course
        sharedPreferences.edit().putInt("nbNotifs",sharedPreferences.getInt("nbNotifs",1) + 1).apply()
        return browserIntent
    }

    /**
     * Triggers an alarm for the next Learning session
     * TODO: GET FREQUENCY FROM SHAREDPREF
     */
    private fun triggerAlarm() {

        val x = 3
        val intent = Intent(this, LearningService::class.java)
        val pendingIntent = PendingIntent.getService(this,1, intent, pendingFlag)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + x * 1000, pendingIntent)
    }
}