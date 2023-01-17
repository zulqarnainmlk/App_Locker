package services

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import appusage.Utils
import helper.Constants
import helper.Sharepref

class DummyService : Service(){
    private var total: Int = 0
    private var todayMinutes: Int = 0
    private var aDayAgoMinutes: Int = 0
    private var twoDaysAgoMinutes: Int = 0
    private var threeDaysAgoMinutes: Int = 0
    private var fourDaysAgoMinutes: Int = 0
    private var fiveDaysAgoMinutes: Int = 0
    private var sixDaysAgoMinutes: Int = 0
    private var sevenDaysAgoMinutes: Int = 0
    private var week1Minutes: Int = 0
    private var week2Minutes: Int = 0
    private var week3Minutes: Int = 0
    private var week4Minutes: Int = 0

    private var totalUsage: Int = 0
    private var totalUsage1: Int = 0
    private var totalUsage2: Int = 0
    private var totalUsage3: Int = 0
    private var totalUsage4: Int = 0
    private var totalUsage5: Int = 0
    private var totalUsage6: Int = 0
    private var totalUsage7: Int = 0
    private var week3Usage: Int = 0
    private var week2Usage: Int = 0
    private var week1Usage: Int = 0
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                weeklyData()
                monthlyData()
        return super.onStartCommand(intent, flags, startId)

    }
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun weeklyData() {

            agoPackageProvider()
            twoDaysAgoPackageProvider()
            threeDaysAgoPackageProvider()
            fourDaysAgoPackageProvider()
            fiveDaysAgoPackageProvider()
            sixDaysAgoPackageProvider()
            sevenDaysAgoPackageProvider()

    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun monthlyData() {

            week3PackageProvider()
            week2PackageProvider()
            week1PackageProvider()

    }
    //////////weekly data one by one////////////////////////////////////////
    //oneDayAgo
    @RequiresApi(Build.VERSION_CODES.N)
    private fun agoPackageProvider(){
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage1=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent1 = aDayAgo(packageName)
                aDayAgoUsageTime(timeSpent1)


            }
        }
        ////////yesterday

        timeShowInFormat(totalUsage1)
        totalMinutesToday(totalUsage1)
        total=totalMinutesToday(totalUsage1)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","twoDaysTotal:"  + totalMinutesToday(totalUsage1).toString())
        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_ago:")
            aDayAgoMinutes=total-todayMinutes
            Log.e("timeInMinutes", "aDayAgoMinutes:$aDayAgoMinutes")
            Sharepref.setString(this, Constants.DAY_AGO_TIME_MINUTES, aDayAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_ago:")
            aDayAgoMinutes=todayMinutes-total
            Log.e("timeInMinutes", "yesterdayMinutes:$aDayAgoMinutes")
            Sharepref.setString(this, Constants.DAY_AGO_TIME_MINUTES, aDayAgoMinutes.toString())
        }

    }
    private fun aDayAgoUsageTime(time: Int) {
        totalUsage1 += time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun aDayAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -1)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //twoDaysAgo
    @RequiresApi(Build.VERSION_CODES.N)
    private fun twoDaysAgoPackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage2=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = twoDaysAgo(packageName)
                twoDaysAgoUsageTime(timeSpent)


            }
        }

        timeShowInFormat(totalUsage2)
        totalMinutesToday(totalUsage2)
        total=totalMinutesToday(totalUsage2)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","threeDaysTotal:"  + totalMinutesToday(totalUsage2).toString())

        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(this, Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_two_days_ago:")
            twoDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes
            Log.e("timeInMinutes", "twoDaysAgoMinutes:$twoDaysAgoMinutes")
            Sharepref.setString(this, Constants.TWO_DAYS_TIME_MINUTES, twoDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_two_days_ago:")
            twoDaysAgoMinutes=todayMinutes-aDayAgoMinutes-total
            Log.e("timeInMinutes", "twoDaysAgoMinutes:$twoDaysAgoMinutes")
            Sharepref.setString(this, Constants.TWO_DAYS_TIME_MINUTES, twoDaysAgoMinutes.toString())
        }
    }
    private fun twoDaysAgoUsageTime(time: Int){
        totalUsage2+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun twoDaysAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -2)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //three days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun threeDaysAgoPackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage3=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = threeDaysAgo(packageName)
                threeDaysAgoUsageTime(timeSpent)


            }
        }


        timeShowInFormat(totalUsage3)
        totalMinutesToday(totalUsage3)
        total=totalMinutesToday(totalUsage3)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")

        Log.e("timeInMinutes","fourDaysTotal:"  + totalMinutesToday(totalUsage3).toString())
        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(this, Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(this, Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_three_days_ago:")
            threeDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes
            Log.e("timeInMinutes", "threeDaysAgoMinutes:$threeDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.THREE_DAYS_TIME_MINUTES, threeDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_three_days_ago:")
            threeDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-total
            Log.e("timeInMinutes", "threeDaysAgoMinutes:$threeDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.THREE_DAYS_TIME_MINUTES, threeDaysAgoMinutes.toString())
        }
    }
    private fun threeDaysAgoUsageTime(time: Int){
        totalUsage3+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun threeDaysAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -3)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //four days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fourDaysAgoPackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage4=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = fourDaysAgo(packageName)
                fourDaysAgoUsageTime(timeSpent)
            }
        }
        timeShowInFormat(totalUsage4)
        totalMinutesToday(totalUsage4)
        total=totalMinutesToday(totalUsage4)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","fiveDaysTotal:"  + totalMinutesToday(totalUsage4).toString())
        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(this, Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(this, Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(this, Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_four_days_ago:")
            fourDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes
            Log.e("timeInMinutes", "fourDaysAgoMinutes:$fourDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.FOUR_DAYS_TIME_MINUTES, fourDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_four_days_ago:")
            fourDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-total
            Log.e("timeInMinutes", "fourDaysAgoMinutes:$fourDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.FOUR_DAYS_TIME_MINUTES, fourDaysAgoMinutes.toString())
        }
    }
    private fun fourDaysAgoUsageTime(time: Int){
        totalUsage4+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fourDaysAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -4)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //five days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fiveDaysAgoPackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage5=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = fiveDaysAgo(packageName)
                fiveDaysAgoUsageTime(timeSpent)
            }
        }
        timeShowInFormat(totalUsage5)
        totalMinutesToday(totalUsage5)
        total=totalMinutesToday(totalUsage5)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","sixDaysTotal:"  + totalMinutesToday(totalUsage5).toString())

        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(this, Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(this, Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(this, Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        fourDaysAgoMinutes=  Sharepref.getString(this,
            Constants.FOUR_DAYS_TIME_MINUTES, "")!!.toInt()
        Log.e("timeInMinutes", "todayMinutes:"+todayMinutes)
        Log.e("timeInMinutes","aDayAgoMinutes:"  + aDayAgoMinutes)
        Log.e("timeInMinutes","twoDaysAgoMinutes:"  + twoDaysAgoMinutes)
        Log.e("timeInMinutes","threeDaysAgoMinutes:"  + threeDaysAgoMinutes)
        Log.e("timeInMinutes","fourDaysAgoMinutes:"  +  fourDaysAgoMinutes)
        if(total!=0)
        {
            Log.e("timeInMinutes","if_five_days_ago:")
            fiveDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes
            Log.e("timeInMinutes", "fiveDaysAgoMinutes:$fiveDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.FIVE_DAYS_TIME_MINUTES, fiveDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_five_days_ago:")
            fiveDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-total
            Log.e("timeInMinutes", "fiveDaysAgoMinutes:$fiveDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.FIVE_DAYS_TIME_MINUTES, fiveDaysAgoMinutes.toString())
        }
    }
    private fun fiveDaysAgoUsageTime(time: Int){
        totalUsage5+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fiveDaysAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -5)
        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //six days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sixDaysAgoPackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage6=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = sixDaysAgo(packageName)
                sixDaysAgoUsageTime(timeSpent)
            }
        }
        timeShowInFormat(totalUsage6)
        totalMinutesToday(totalUsage6)
        total=totalMinutesToday(totalUsage6)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","sevenDaysTotal:"  + totalMinutesToday(totalUsage6).toString())
        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(this, Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(this, Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(this, Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        fourDaysAgoMinutes=  Sharepref.getString(this,
            Constants.FOUR_DAYS_TIME_MINUTES, "")!!.toInt()
        fiveDaysAgoMinutes=  Sharepref.getString(this,
            Constants.FIVE_DAYS_TIME_MINUTES, "")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_six_days_ago:")
            sixDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes
            Log.e("timeInMinutes", "sixDaysAgoMinutes:$sixDaysAgoMinutes")
            Sharepref.setString(this, Constants.SIX_DAYS_TIME_MINUTES, sixDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_six_days_ago:")
            sixDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes-total
            Log.e("timeInMinutes", "sixDaysAgoMinutes:$sixDaysAgoMinutes")
            Sharepref.setString(this, Constants.SIX_DAYS_TIME_MINUTES, sixDaysAgoMinutes.toString())
        }
    }
    private fun sixDaysAgoUsageTime(time: Int){
        totalUsage6+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sixDaysAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -6)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //seven days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sevenDaysAgoPackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        totalUsage7=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = sevenDaysAgo(packageName)
                sevenDaysAgoUsageTime(timeSpent)
            }
        }
        timeShowInFormat(totalUsage7)
        Sharepref.setString(this, Constants.WEEKLY_TOTAL_TIME,timeShowInFormat(totalUsage7))
        Sharepref.setString(this, Constants.WEEK4_TOTAL_MINUTES,totalMinutesToday(totalUsage7).toString())
        total=totalMinutesToday(totalUsage7)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","eightDaysTotal:"  + totalMinutesToday(totalUsage7).toString())
        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(this, Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(this, Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(this, Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(this, Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        fourDaysAgoMinutes=  Sharepref.getString(this,
            Constants.FOUR_DAYS_TIME_MINUTES, "")!!.toInt()
        fiveDaysAgoMinutes=  Sharepref.getString(this,
            Constants.FIVE_DAYS_TIME_MINUTES, "")!!.toInt()
        sixDaysAgoMinutes=  Sharepref.getString(this, Constants.SIX_DAYS_TIME_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_seven_days_ago:")
            sevenDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes-sixDaysAgoMinutes
            Log.e("timeInMinutes", "sevenDaysAgoMinutes:$sevenDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.SEVEN_DAYS_TIME_MINUTES, sevenDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_seven_days_ago:")
            sevenDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes-sixDaysAgoMinutes-total
            Log.e("timeInMinutes", "sevenDaysAgoMinutes:$sevenDaysAgoMinutes")
            Sharepref.setString(this,
                Constants.SEVEN_DAYS_TIME_MINUTES, sevenDaysAgoMinutes.toString())
        }
    }
    private fun sevenDaysAgoUsageTime(time: Int){
        totalUsage7+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sevenDaysAgo(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -7)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //////////////////////////////////////////////Monthly data week per week////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////week3/////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week3PackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        week3Usage=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = week3(packageName)
                week3UsageTime(timeSpent)
            }
        }
//        timeShowInFormat(week3Usage)
//        Sharepref.setString(this,Constants.WEEKLY_TOTAL_TIME,timeShowInFormat(week3Usage))
        totalMinutesToday(week3Usage)
        total=totalMinutesToday(week3Usage)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","week3Total:"  + totalMinutesToday(week3Usage).toString())
        //today minutes get by sharepref

        week4Minutes=   Sharepref.getString(this, Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_week3:")
            week3Minutes=total-week4Minutes
            Log.e("timeInMinutes", "week3Minutes:$week3Minutes")
            Sharepref.setString(this, Constants.WEEK3_TOTAL_MINUTES, week3Minutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_week3:")
            week3Minutes=week4Minutes-total
            Log.e("timeInMinutes", "week3Minutes:$week3Minutes")
            Sharepref.setString(this, Constants.WEEK3_TOTAL_MINUTES, week3Minutes.toString())
        }
    }
    private fun week3UsageTime(time: Int){
        week3Usage+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week3(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -14)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    ///////////////////////////////////////////week2////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week2PackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        week2Usage=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = week2(packageName)
                week2UsageTime(timeSpent)
            }
        }
//        timeShowInFormat(week3Usage)
//        Sharepref.setString(this,Constants.WEEKLY_TOTAL_TIME,timeShowInFormat(week3Usage))
        totalMinutesToday(week2Usage)
        total=totalMinutesToday(week2Usage)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","week2Total:"  + totalMinutesToday(week2Usage).toString())
        //today minutes get by sharepref

        week3Minutes=   Sharepref.getString(this, Constants.WEEK3_TOTAL_MINUTES, "")!!.toInt()
        week4Minutes=   Sharepref.getString(this, Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_week2:")
            week3Minutes=total-week4Minutes-week3Minutes
            Log.e("timeInMinutes", "week2Minutes:$week2Minutes")
            Sharepref.setString(this, Constants.WEEK2_TOTAL_MINUTES, week2Minutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_week2:")
            week3Minutes=week4Minutes-week3Minutes-total
            Log.e("timeInMinutes", "week2Minutes:$week2Minutes")
            Sharepref.setString(this, Constants.WEEK2_TOTAL_MINUTES, week2Minutes.toString())
        }
    }
    private fun week2UsageTime(time: Int){
        week2Usage+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week2(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -21)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    ////////////////////////////////////////week1//////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week1PackageProvider() {
        val packageManager = this.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        week1Usage=0
        for (i in packageInfoList.indices) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val packageName = packageInfo.packageName

                val timeSpent = week1(packageName)
                week1UsageTime(timeSpent)
            }
        }

        totalMinutesToday(week1Usage)
        timeShowInFormat(week1Usage)
        Sharepref.setString(this, Constants.MONTHLY_TOTAL_TIME, timeShowInFormat(week1Usage))
        total=totalMinutesToday(week1Usage)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","week1Total:"  + totalMinutesToday(week1Usage).toString())
        //today minutes get by sharepref
        week2Minutes=   Sharepref.getString(this, Constants.WEEK2_TOTAL_MINUTES, "")!!.toInt()
        week3Minutes=   Sharepref.getString(this, Constants.WEEK3_TOTAL_MINUTES, "")!!.toInt()
        week4Minutes=   Sharepref.getString(this, Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_week1:")
            week1Minutes=total-week4Minutes-week3Minutes-week2Minutes
            Log.e("timeInMinutes", "week1Minutes:$week1Minutes")
            Sharepref.setString(this, Constants.WEEK1_TOTAL_MINUTES, week1Minutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_week1:")
            week1Minutes=week4Minutes-week3Minutes-week2Minutes-total
            Log.e("timeInMinutes", "week1Minutes:$week1Minutes")
            Sharepref.setString(this, Constants.WEEK1_TOTAL_MINUTES, week1Minutes.toString())
        }
    }
    private fun week1UsageTime(time: Int){
        week1Usage+= time
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week1(packageName: String):Int {
        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -28)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(this, packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    ///////////////////////////////////////////formation methods////////////////////////////
    private fun timeShowInFormat(timeSpent: Int): String {
        val timesAllowed = Utils.reverseProcessTime(timeSpent)
        Log.e("time:",timesAllowed.toString())

        var hour = timesAllowed[0].toString()
        var mint = timesAllowed[1].toString()
        var sec = timesAllowed[2].toString()

        if (hour.toInt() in 0..9) {
            hour = "0$hour"
        }
        if (mint.toInt() in 0..9) {
            mint = "0$mint"
        }
        if (sec.toInt() in 0..9) {
            sec = "0$sec"
        }
        //timeConvertor(hour,mint)
        return "${hour}h : ${mint}m "
        //": ${sec}s"

    }
    private fun totalMinutesToday(timeSpent: Int): Int {
        val timesAllowed = Utils.reverseProcessTime(timeSpent)
        Log.e("time:",timesAllowed.toString())

        var hour = timesAllowed[0].toString()
        var mint = timesAllowed[1].toString()
        var sec = timesAllowed[2].toString()
        var totalMinutes=hour.toInt()*60+mint.toInt()
        //timeConvertor(hour,mint)
        return totalMinutes
        //": ${sec}s"

    }

}
