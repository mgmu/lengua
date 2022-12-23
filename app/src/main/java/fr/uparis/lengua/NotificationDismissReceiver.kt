package fr.uparis.lengua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 * This class is used to receive intents sent by swiping a notification
 * */
class NotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("LOGLENGUASERVICE2", "NotificationDismissReceiver::onReceive()")
        if (intent.extras != null) { // launch update word service with given extras
            val wordUpdateIntent = Intent(context, WordUpdateService::class.java)
            wordUpdateIntent.putExtras(intent.extras!!)
            context.startService(wordUpdateIntent)
        }
    }
}