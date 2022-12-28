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
        Log.d("LOGLENGUASERVICE2", "NDR::onReceive()")
        if (intent.extras != null) { // launch update word service with given extras
            Log.d("LOGLENGUASERVICE2", "NDR::intent.extras not null")
            val wordUpdateIntent = Intent(context, LearningService2::class.java)
                .apply { action = "update" }
            wordUpdateIntent.putExtras(intent.extras!!)
            context.startService(wordUpdateIntent)
        } else Log.d("LOGLENGUASERVICE2", "NDR::intent.extras null")
    }
}