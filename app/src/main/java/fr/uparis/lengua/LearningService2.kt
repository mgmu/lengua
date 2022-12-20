package fr.uparis.lengua

import android.content.Intent
import android.os.IBinder
import android.util.Log
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

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        allWordsInDB = dao.loadAllWords()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d(TAG, "onStartCommand()")

        /* Load NOTIFICATIONSTODISPLAY*/
        allWordsInDB.observe(this) {
            if (!notificationsDisplayed)
                displayNotifications()
        }

        return START_NOT_STICKY
    }

    private fun displayNotifications() {
        notificationsDisplayed = true
        Log.d(TAG, "displayNotifications()")
        Log.d(TAG, "words to notify: ${drawRandomWords()}")
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

    /* Returns a list containing at most notificationsToDisplay words. */
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