package fr.uparis.lengua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * This broadcastreceiver catches the dismiss notification event.
 */
class NotificationDismissReceiver : BroadcastReceiver() {

    private lateinit var dao:IDao;
    /**
     * This method updates the apparition frequence of the skipped word.
     * TODO UPDATE THE WORD FREQUENCY IN THE DATABASE FIND A WAY TO GET DAO
     */
    override fun onReceive(context: Context, intent: Intent) {
//        dao = (context as TranslationApplication).database.iDao()
        val word = intent.extras!!.getString("word","incorrect word")
//        val word1 = dao.selectWord(word).value
        Log.d("logLENGUA","Dissmissed notification : $word")

    }

    private fun updateFrequecy(word:Word){
        dao.updateWord(word)
    }
}