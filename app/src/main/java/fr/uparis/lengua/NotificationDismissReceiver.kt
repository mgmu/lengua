package fr.uparis.lengua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * This class is used to receive intents sent by swiping a notification
 * */
class NotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.extras != null) { // launch update word service with given extras
            val wordUpdateIntent = Intent(context, LearningService2::class.java)
                .apply { action = "update" }
            wordUpdateIntent.putExtras(intent.extras!!)
            context.startService(wordUpdateIntent)
        }
    }
}