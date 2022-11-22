package appusage

import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock

internal object Alarms {
    @JvmStatic
    fun scheduleNotification(context: Context) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(), (60 * 1000).toLong(), alarmIntent
        )
    } //    @RequiresApi(api = Build.VERSION_CODES.N)
    //    static void resetIsUsageExceededData(Context context) {
    //        Intent intent = new Intent(context, ResetDataReceiver.class);
    //        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
    //
    //        Calendar calendar = Calendar.getInstance();
    //        calendar.set(Calendar.HOUR_OF_DAY, 0);
    //        calendar.set(Calendar.MINUTE, 0);
    //        calendar.set(Calendar.SECOND, 0);
    //
    //        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    //        if (alarmManager != null) {
    //            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
    //                    AlarmManager.INTERVAL_DAY, alarmIntent);
    //        }
    //    }
}