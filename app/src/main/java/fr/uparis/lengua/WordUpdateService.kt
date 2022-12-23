package fr.uparis.lengua

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData

/**
 * This service is started by the NotificationDismissReceiver in order
 * to:
 *     - update notification displayed with new word
 *     - update number of times the given word has been swiped
 */
class WordUpdateService : LifecycleService() {
    private val dao by lazy {(application as TranslationApplication).database.iDao()}
    private lateinit var allWordsInDB: LiveData<List<Word>> /* All the words in the DB. */

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        allWordsInDB = dao.loadAllWords()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("LOGLENGUASERVICE2", "WordUpdateService::onStartCommand()")

        allWordsInDB.observe(this) {
            // draw random word and replace it
            // update nb swipes in db
        }

        return START_NOT_STICKY
    }
}