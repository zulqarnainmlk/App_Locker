package appusage

import appusage.BackgroundService.Companion.enqueueWork
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        enqueueWork(context, Intent(context, BackgroundService::class.java))
    }
}