package appusage

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.math.RoundingMode
import java.text.DecimalFormat

internal object Utils {

    @JvmField
    var DAY_IN_MILLIS = (86400 * 1000).toLong()
    var SEVEN_DAY_IN_MILLIS = (604800 * 1000).toLong()
    fun processTime(hour: Int, minute: Int, second: Int): Int {
        return hour * 3600 + minute * 60 + second
    }

    @JvmStatic
    fun reverseProcessTime(time: Int): IntArray {
        var time = time
        Log.e("time1122", "timetotalusage${time}")

        val hourMinSec = IntArray(3)
        hourMinSec[0] = time / 3600
        time = time % 3600
        hourMinSec[1] = time / 60
        hourMinSec[2] = time % 60



        return hourMinSec
    }

    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getTimeSpent(
        context: Context,
        packageName: String?,
        beginTime: Long,
        endTime: Long
    ): HashMap<String?, Int?> {
        var currentEvent: UsageEvents.Event
        val allEvents: MutableList<UsageEvents.Event> = ArrayList()
        val appUsageMap = HashMap<String?, Int?>()
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageEvents = usageStatsManager.queryEvents(beginTime, endTime)
        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.packageName == packageName || packageName == null) {
                if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                    || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                ) {
                    allEvents.add(currentEvent)
                    val key = currentEvent.packageName
                    if (appUsageMap[key] == null) appUsageMap[key] = 0
                }
            }
        }
        for (i in 0 until allEvents.size - 1) {
            val E0 = allEvents[i]
            val E1 = allEvents[i + 1]
            if (E0.eventType == UsageEvents.Event.ACTIVITY_RESUMED && E1.eventType == UsageEvents.Event.ACTIVITY_PAUSED && E0.className == E1.className) {
                var diff = (E1.timeStamp - E0.timeStamp).toInt()
                diff /= 1000
                var prev = appUsageMap[E0.packageName]
                if (prev == null) prev = 0
                appUsageMap[E0.packageName] = prev + diff
            }
        }
        if (allEvents.size != 0) {
            val lastEvent = allEvents[allEvents.size - 1]
            if (lastEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                val currentRunningPackageName = lastEvent.packageName
                var diff = System.currentTimeMillis().toInt() - lastEvent.timeStamp.toInt()
                diff /= 1000
                var prev = appUsageMap[currentRunningPackageName]
                if (prev == null) prev = 0
                appUsageMap[currentRunningPackageName] = prev + diff
                appUsageMap["current$currentRunningPackageName"] = -1 //for notification purpose
            }
        }
        return appUsageMap
    }
    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }
}