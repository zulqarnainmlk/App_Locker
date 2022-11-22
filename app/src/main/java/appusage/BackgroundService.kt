package appusage

import android.app.Notification
import appusage.Utils.getTimeSpent
import androidx.core.app.JobIntentService
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.app.NotificationManager
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import com.example.app_locker.R
import java.util.stream.Collectors

class BackgroundService : JobIntentService() {
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onHandleWork(intent: Intent) {
//        dbHelper = new DatabaseHelper(this);
//        List<TrackedAppInfo> trackedAppInfos = dbHelper.getAllRows();
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val beginTime = calendar.timeInMillis
        val endTime = beginTime + Utils.DAY_IN_MILLIS
        val appUsageMap = getTimeSpent(this, null, beginTime, endTime)
        var currentRunningPackageName: String? = null
        val list = appUsageMap.keys.stream().filter { s: String? -> s!!.startsWith("current") }
            .collect(Collectors.toList())
        if (list.size > 0) {
            currentRunningPackageName = list[0]!!.replaceFirst("current".toRegex(), "")
        }

//        for(int i = 0; i < trackedAppInfos.size(); i++) {
        // TrackedAppInfo trackedAppInfo = trackedAppInfos.get(i);
        val packageName = ""
        if (appUsageMap.containsKey(packageName)) {
            var usageTime = appUsageMap[packageName]
            if (usageTime == null) usageTime = 0
            val allowedTime = 0
            val isUsageExceeded = 0
            if (usageTime > allowedTime || packageName == currentRunningPackageName) {
                try {
                    // dbHelper.setIsUsageExceeded(packageName);
                    val appName = packageManager
                        .getApplicationLabel(
                            packageManager.getApplicationInfo(
                                packageName,
                                0
                            )
                        ) as String
                    showNotification(appName, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(TAG, "package name not found")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // dbHelper.close();
    }

    private fun showNotification(appName: String, id: Int) {
        val builder = Notification.Builder(applicationContext)
            .setContentTitle("$appName usage exceeded!")
            .setContentText("Close your app now!")
            .setSmallIcon(R.drawable.warning)
            .setPriority(Notification.PRIORITY_MAX)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, builder.build())
    }

    companion object {
        //private DatabaseHelper dbHelper;
        private const val TAG = "BackgroundService"
        private const val JOB_ID = 1
        @JvmStatic
        fun enqueueWork(context: Context?, intent: Intent?) {
            enqueueWork(context!!, BackgroundService::class.java, JOB_ID, intent!!)
        }
    }
}