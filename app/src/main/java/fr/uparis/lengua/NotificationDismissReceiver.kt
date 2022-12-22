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
        Log.d("LOG_LENGUA", "NotificationDismissReceiver::onReceive()")
    }
}