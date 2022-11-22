package appusage

import android.graphics.drawable.Drawable

 data class AppInfoModel
// internal constructor
     (
    val appName: String,
    val icon: Drawable,
    val packageName: String,
    val hour: String,
    val mint: String,
    val sec: String,
    val timeSpent:Int
)
//     : Comparable<AppInfo> {
//    private val isTracked = false
//    private val isUsageExceeded = false
//    override fun compareTo(appInfo: AppInfo): Int {
//        return appName.compareTo(appInfo.appName)
//    }
//}