package fr.uparis.lengua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * This class is used to receive intents sent by swiping a notification
 * */
class NotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("", "NotificationDismissReceiver::onReceive()")
        if (intent.extras != null) {
            with (intent.extras!!) {
                val word = getString(context.getString(R.string.word_word_key))
                if (word != null)
                    Log.d("LOGLENGUASERVICE2", "word provided in intent $word")
                val src_language = getString(context.getString(R.string.word_src_language_key))
                if (src_language != null)
                    Log.d("LOGLENGUASERVICE2", "src provided in intent $src_language")
                val dest_language = getString(context.getString(R.string.word_src_language_key))
                if (dest_language != null)
                    Log.d("LOGLENGUASERVICE2", "dst provided in intent $dest_language")
                val id = getInt(context.getString(R.string.notification_id_key))
                if (id != null)
                    Log.d("LOGLENGUASERVICE2", "id provided in intent $id")
            }
        }
    }
}