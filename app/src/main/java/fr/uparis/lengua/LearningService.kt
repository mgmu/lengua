package fr.uparis.lengua

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.random.Random
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import android.net.Uri

/**
 * This service allows the user to review words by using notifications.
 */
class LearningService : Service() {


    private val REMAINING_NOTIFICATIONS = "number of notifications to send for next course"
    private val HOUR_ = "hour"
    private val MINUTE_ = "minute"
    private val RECAP_FREQUENCY_ = "repeatingInterval"

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
    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Deciding which flag to use depending on sdk version
     */
    private val pendingFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        PendingIntent.FLAG_IMMUTABLE
    else
        PendingIntent.FLAG_UPDATE_CURRENT

    /*Getting dao */
    private val dao: IDao by lazy {
        (application as TranslationApplication).database.iDao()
    }
    private var alreadyGivenWords :MutableList<Int> = mutableListOf()



    private val dismissReceiver:NotificationDismissReceiver = NotificationDismissReceiver()

    /**
     * Notification channel creation before starting service
     */
    override fun onCreate() {
        super.onCreate()
        Log.d("logLENGUA","onCreateService")
        createNotificationChannel()
        fillWords()
        getApplication().registerReceiver(dismissReceiver, IntentFilter("dismiss"))
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("logLENGUA","onstartCommand")

        // the actual lesson will not be influenced by the last lesson choices
        alreadyGivenWords.clear()
        sharedPreferences.edit().putInt(REMAINING_NOTIFICATIONS,10).apply()
        var remainingPlace =  sharedPreferences.getInt(REMAINING_NOTIFICATIONS,3)
        sendNotifications(remainingPlace)
        triggerAlarm()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    /**
     * Fills words list manually for tests
     * TODO GET WORDS FROM DATABASE
     */
    private fun fillWords() {
//        words = dao.loadAllWords().value ?: listOf()
//        Log.d("logLENGUA", dao.toString())

        val word = Word(
            "Film", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Film"
        )
        val w = Word(
            "Truc", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Truc"
        )

        val w1 = Word(
            "Livre", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Livre"
        )

        val w2 = Word(
            "Bonjour", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Bonjour"
        )

        val w3 = Word(
            "Maison", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Maison"
        )

        val w4 = Word(
            "Cahier", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Cahier"
        )

        val w5 = Word(
            "Voiture", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Voiture"
        )

        val w6 = Word(
            "Lampe", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Lampe"
        )

        val w7 = Word(
            "Test", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Test"
        )

        val w8 = Word(
            "Lit", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Lit"
        )

        val w9 = Word(
            "Mousse", "fr", "es",
            "https://www.reverso.net/traduction-texte#sl=fra&tl=spa&text=Mousse"
        )

        words = listOf(w, w1, w2, w3, w4, w5, w6, w7, w8, w9, word)

    }

    /**
     * Draws a random word from the database
     * It ensures that this word will not be drawn twice for a same lesson
     * @return the drawn word
     */
    private fun getRandomWord(): Word{
        var position = 0;
        while (position in alreadyGivenWords)
            position = Random.nextInt(words.size)
        return words[position]
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
        var pos = words.indexOf(word)
        alreadyGivenWords.add(pos) // from here this word will not be drawn a second time for this
                                  // lesson

        val swipeIntent =  Intent(this,NotificationDismissReceiver::class.java)

        swipeIntent.putExtra("word",word.word).apply {
                action = "dismiss"
        }

        val notif = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(word.word)
            .setContentText("Tap to see translation")
            .setSmallIcon(androidx.core.R.drawable.notification_bg_low_normal) // should find a icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(
        PendingIntent.getActivity(this,0, launchBrowser(word.link),
            pendingFlag))
            .setDeleteIntent(PendingIntent.getBroadcast(this,currentNotificationID, swipeIntent,
           pendingFlag))
            .build()
        currentNotificationID++
        return notif
    }

    /**
     * Sends notifications used to see the translation of a given word
     * @param nbOfNotifications will be sent
     */
    private fun sendNotifications(nbOfNotifications:Int) {
        for (i in 0 until nbOfNotifications) {
            Thread.sleep(500)
            notificationManager.notify(currentNotificationID, createNotification())

            // each notification should take a place in the number of notification to send
            var sentLeft = sharedPreferences.getInt(REMAINING_NOTIFICATIONS, nbOfNotifications)
            sharedPreferences.edit().putInt(REMAINING_NOTIFICATIONS, sentLeft-1).apply()
        }
    }

    /**
     * Searches the given address on the web
     * @param address to look for
     * @return the intent that launches the browser
     */
    private fun launchBrowser(address:String):Intent {

        val url = Uri.parse(address)
        val browserIntent = Intent(Intent.ACTION_VIEW,url)

        // The notification is used so we free the place for an other notification
        // in the next course
        val reamainingNotification = sharedPreferences.getInt(REMAINING_NOTIFICATIONS,1)
        sharedPreferences.edit().putInt(REMAINING_NOTIFICATIONS,reamainingNotification + 1).apply()
        return browserIntent
    }

    /**
     * Triggers an alarm for the next Learning session
     * The time info of the alarm come from sharedPreferences
     * If there are not info in sharedPreferences the lesson takes place everyday at 8 O'clock
     */
    private fun triggerAlarm() {
        val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, sharedPreferences.getInt(HOUR_,8))
            set(Calendar.MINUTE,sharedPreferences.getInt(MINUTE_,0))
        }

        val intent = Intent(this, LearningService::class.java)
        val pendingIntent = PendingIntent.getService(this,1, intent, pendingFlag)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            sharedPreferences.getLong(RECAP_FREQUENCY_, AlarmManager.INTERVAL_FIFTEEN_MINUTES),
            pendingIntent
        )
        Log.d("logLENGUA", "END OF TRIGGER ALARM")
    }
}
