package fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import appusage.*
import appusage.Utils.reverseProcessTime
import com.example.app_locker.R
import database.VaultDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import listeners.DialogListener
import models.AppVault

import kotlin.collections.ArrayList


class HomeFragment : Fragment(), View.OnClickListener, DialogListener {
    private var packageName1: String? = null
    private var totalUsage: Int = 0

    private val DPM_ACTIVATION_REQUEST_CODE = 6135
    private val vaultDatabase by lazy { VaultDatabase.getDatabase(requireContext()).vaultDao() }
    private val TAG = "PermissionDemo"
    private val PERMISSION_APPUSAGE_REQUEST_CODE = 101
    private val PERMISSION_ACCESSIBILITY_REQUEST_CODE = 102
    companion object {
        const val LAUNCH_SETTINGS_ACTIVITY = 1
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_home.text = getString(R.string.home_frag)

        Handler().postDelayed({
            init()
            progressBar.visibility=View.GONE
        }, Constants.DELAY_TIME.toLong())



    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        checkRunTimePermission()
        listeners()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            byDefaultDataShow()
        }
        startBackgroundService()
//        weeklyData()
//        monthlyData()
    }





    @RequiresApi(Build.VERSION_CODES.N)
    private fun byDefaultDataShow() {
        tab_today.setHintTextColor(resources.getColor(R.color.color_green))
        today_underline.setBackgroundColor(resources.getColor(R.color.color_green))
        showTodayList()

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkRunTimePermission(){


        val usage_permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.PACKAGE_USAGE_STATS
        )
        val accessibility_permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BIND_ACCESSIBILITY_SERVICE
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (accessibility_permission != PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(requireContext(),"Permission already granted",Toast.LENGTH_SHORT).show()
            Log.e("tag1122", "not-granted")
//             openUsageDialog()
            Dialog.openUsageDialog(requireContext(), this)
           // listPermissionsNeeded.add(Manifest.permission.PACKAGE_USAGE_STATS)

        }
        if (usage_permission != PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(requireContext(),"Permission already granted",Toast.LENGTH_SHORT).show()
            Log.e("tag1122", "not-granted")
//             openUsageDialog()
            Dialog.openAccessibilityDialog(requireContext(), this)
            //listPermissionsNeeded.add(Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
        }
//        if (listPermissionsNeeded.isNotEmpty()) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                listPermissionsNeeded.toTypedArray(),
//                1
//            )
//            return false
//        }


        else {
            Log.e("tag1122", "granted")
            Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT)
                .show()

        }
  //      return true

    }
    override fun dialogListerData(key: String,activity:Activity) {
        when (key) {
            "permission" -> {
                val usageAccessIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivityForResult(usageAccessIntent, LAUNCH_SETTINGS_ACTIVITY)
            }
            "permission1" -> {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SETTINGS_ACTIVITY) {
            Alarms.scheduleNotification(requireContext())
        }
    }
    private fun startBackgroundService() {
        Alarms.scheduleNotification(requireContext())
       // activity?.startService(Intent(activity, MyService::class.java))
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////recyclerview///////////////////////////////////////////////////////////////////////////
    @get:RequiresApi(api = Build.VERSION_CODES.N)
    private val todayUsageList: List<AppInfoModel>
        private get() {
            val packageManager = requireContext().packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            val todayUsageList: ArrayList<AppInfoModel> = ArrayList<AppInfoModel>()
            totalUsage = 0

            for (i in packageInfoList.indices) {
                val packageInfo = packageInfoList[i]
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val packageName = packageInfo.packageName
                    val AppId = System.currentTimeMillis().toInt()
                    val data = AppVault(
                        AppId, appName, packageName
                    )
                    lifecycleScope.launch {
                        vaultDatabase.addAppData(data)

                        // blockApp(position)
                    }
                    Log.e("Tag14", "---------------------------------------")
                    Log.e("Tag14", "packageName $packageName")
                    ///////////today

                    val timeSpent = todayTimeSpent(packageName)
                    Log.e("Tag16", "timetotalusage${timeSpent}")
                    totalUsageTime(timeSpent)
                    val timesAllowed = reverseProcessTime(timeSpent)
                    Log.e("Time1:", "timetotalusage${timesAllowed}")

                    val hour = timesAllowed[0].toString()
                    val mint = timesAllowed[1].toString()
                    val sec = timesAllowed[2].toString()
                    if(hour=="0"&&mint=="0"&&sec=="0") {
                        //todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                        //Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()

                    }
                    else
                    {
                        todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                    }
                }
            }

            specificAppUsage()
            Log.e("specificAppUsage", "specificAppUsage ${specificAppUsage()}")
            Log.e("specificAppUsage", "totalUsage ${totalUsage}")
            timeShowInFormat(totalUsage)
            //total usage
            Log.e("specificAppUsage",  timeShowInFormat(totalUsage))
            Sharepref.setString(requireContext(),Constants.TODAY_TIME_MINUTES,totalMinutesToday(totalUsage).toString())
            Sharepref.setString(requireContext(),Constants.TODAY_TOTAL_TIME,timeShowInFormat(totalUsage))
            ///////////////////////////////////////////////////////////////////////////////
            totalMinutesToday(totalUsage)
            Log.e("timeInMinutes",  "todayMinutes:"+ totalMinutesToday(totalUsage).toString())
///////////////////////////////////////////////////////////////////////////////////////////////
            total_time.text = timeShowInFormat(totalUsage)
            return todayUsageList
        }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun todayTimeSpent(packageName: String): Int {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val beginTime = calendar.timeInMillis
        val endTime = beginTime + Utils.DAY_IN_MILLIS
        Log.e("begintime:::::", beginTime.toString())
        Log.e("endtime:::::",endTime.toString())
        val appUsageMap = Utils.getTimeSpent(requireContext(), packageName, beginTime, endTime)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun showTodayList() {
        usage_recycler.layoutManager = LinearLayoutManager(requireActivity())

        val appInfoList = todayUsageList
//        appInfoList.sortedByDescending { it.timeSpent }
//
        val mutableAppInfoList = appInfoList.toMutableList()
        mutableAppInfoList.sortByDescending { it.timeSpent }
        val appInfoListAdapter = UsageAdapter(mutableAppInfoList as ArrayList<AppInfoModel>)
        usage_recycler!!.adapter = appInfoListAdapter
//        appListView!!.onItemClickListener =
//            AdapterView.OnItemClickListener { adapterView, view, i, l ->
//
//                Log.e("abc", "package name" + appInfoList[i].packageName)
//                Sharepref.setString(
//                    requireContext(),
//                    Constants.CURRENT_PACKAGE_NAME,
//                    appInfoList[i].packageName
//                )
//                getString(requireContext(), Constants.CURRENT_PACKAGE_NAME, "")
//                Log.e(
//                    "abc",
//                    "current package name" + getString(
//                        requireContext(),
//                        Constants.CURRENT_PACKAGE_NAME,
//                        ""
//                    )
//                )
//                Sharepref.setString(
//                    requireContext(),
//                    Constants.CURRENT_APP_NAME,
//                    appInfoList[i].appName
//                )
//                findNavController().navigate(R.id.action_homeFragment_to_chart)
//
//            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun specificAppUsage(): Int {
        var packageName = "com.example.app_locker"

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val beginTime = calendar.timeInMillis
//        val beginTime =0L

        val endTime = beginTime + Utils.DAY_IN_MILLIS
//        val endTime = 57600000L


        Log.e("TAG123", "beginTime $beginTime")
        Log.e("TAG123", "Utils.DAY_IN_MILLIS ${Utils.DAY_IN_MILLIS}")
        Log.e("TAG123", "endTime $endTime")
        val appUsageMap = Utils.getTimeSpent(requireContext(), packageName, beginTime, endTime)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime


    }
    private fun totalUsageTime(time: Int) {
        totalUsage += time
    }
    private fun timeShowInFormat(timeSpent: Int): String {
        val timesAllowed = reverseProcessTime(timeSpent)
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
        val timesAllowed = reverseProcessTime(timeSpent)
        Log.e("time:",timesAllowed.toString())

        var hour = timesAllowed[0].toString()
        var mint = timesAllowed[1].toString()
        var sec = timesAllowed[2].toString()
        var totalMinutes=hour.toInt()*60+mint.toInt()
        //timeConvertor(hour,mint)
        return totalMinutes
        //": ${sec}s"

    }
    @get:RequiresApi(api = Build.VERSION_CODES.N)
    private val weeklyUsageList: List<AppInfoModel>
        private get() {
            val packageManager = requireContext().packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            val weeklyUsageList: ArrayList<AppInfoModel> = ArrayList<AppInfoModel>()
            totalUsage = 0

            for (i in packageInfoList.indices) {
                val packageInfo = packageInfoList[i]
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val packageName = packageInfo.packageName

                    Log.e("Tag14", "---------------------------------------")
                    Log.e("Tag14", "packageName $packageName")

                    val timeSpent = weeklyTimeSpent(packageName)
                    //val timeSpent1 = yesterdayTimeSpent(packageName)


                    Log.e("Tag16", "timetotalusage${timeSpent}")
                    totalUsageTime(timeSpent)
                    ////////////////////////////////////////////////////////////////////////////////
                   // totalUsageYesterDay(timeSpent1)
                    timeShowInFormat(totalUsage)
                    //totalMinutesYesterday(totalUsage1)


                   // Log.e("totalUsageyesterday",  totalMinutesYesterday(totalUsage1).toString())
                    /////////////////////////////////////////////////////////////////
                    val timesAllowed = reverseProcessTime(timeSpent)
                    Log.e("Time1:", "timetotalusage${timesAllowed}")

                    val hour = timesAllowed[0].toString()
                    val mint = timesAllowed[1].toString()
                    val sec = timesAllowed[2].toString()
                    if(hour=="0"&&mint=="0"&&sec=="0") {
                        //todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                        //Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()

                    }
                    else
                    {
                        weeklyUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                    }
                }
            }
            return weeklyUsageList
        }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun weeklyTimeSpent(packageName: String): Int {

        val beginCal = Calendar.getInstance()
        //val weekCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -7)
        //weekCal.firstDayOfWeek = Calendar.SUNDAY
        Log.e("week", beginCal.timeInMillis.toString())

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(
            requireContext(),
            packageName,
            beginCal.timeInMillis,
            endCal.timeInMillis
        )
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun showWeeklyList() {
        usage_recycler.layoutManager = LinearLayoutManager(requireActivity())

        val appInfoList = weeklyUsageList
        val mutableAppInfoList = appInfoList.toMutableList()
        mutableAppInfoList.sortByDescending { it.timeSpent }
        val appInfoListAdapter = UsageAdapter(mutableAppInfoList as ArrayList<AppInfoModel>)
        usage_recycler!!.adapter = appInfoListAdapter
    }
    private val monthlyUsageList: List<AppInfoModel>
        @RequiresApi(Build.VERSION_CODES.N)
        private get() {
            val packageManager = requireContext().packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            val monthlyUsageList: ArrayList<AppInfoModel> = ArrayList<AppInfoModel>()
            totalUsage = 0
            for (i in packageInfoList.indices) {
                val packageInfo = packageInfoList[i]
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val packageName = packageInfo.packageName
                    Log.e("Tag14", "---------------------------------------")
                    Log.e("Tag14", "packageName $packageName")

                    val timeSpent = monthlyTimeSpent(packageName)

                    Log.e("Tag16", "timetotalusage${timeSpent}")
                    totalUsageTime(timeSpent)

                    val timesAllowed = reverseProcessTime(timeSpent)
                    Log.e("Time1:", "timetotalusage${timesAllowed}")

                    val hour = timesAllowed[0].toString()
                    val mint = timesAllowed[1].toString()
                    val sec = timesAllowed[2].toString()
                    if(hour=="0"&&mint=="0"&&sec=="0") {
                        //todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                        //Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()

                    }
                    else
                    {
                        monthlyUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                    }
                }
            }
            return monthlyUsageList
        }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun monthlyTimeSpent(packageName: String): Int {
//        val calendar = Calendar.getInstance()
//        calendar[Calendar.HOUR_OF_DAY] = 0
//        calendar[Calendar.MINUTE] = 0
//        calendar[Calendar.SECOND] = 0
//        val beginTime =calendar.timeInMillis
//        val endTime = beginTime + Utils.SEVEN_DAY_IN_MILLIS
        val beginCal = Calendar.getInstance()
        beginCal.add(Calendar.DATE, -28)
        val endCal = Calendar.getInstance()



        val appUsageMap = Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun showMonthlyList() {
        usage_recycler.layoutManager = LinearLayoutManager(requireActivity())

        val appInfoList = monthlyUsageList
        val mutableAppInfoList = appInfoList.toMutableList()
        mutableAppInfoList.sortByDescending { it.timeSpent }
        val appInfoListAdapter = UsageAdapter(mutableAppInfoList as ArrayList<AppInfoModel>)
        usage_recycler!!.adapter = appInfoListAdapter
    }
    private fun listeners() {
        tab_today.setOnClickListener(this)
        tab_week.setOnClickListener(this)
        tab_month.setOnClickListener(this)
        graph.setOnClickListener(this)
        home_tab.setOnClickListener(this)
        apps_tab.setOnClickListener(this)
        vault_tab.setOnClickListener(this)
        profile_tab.setOnClickListener(this)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tab_today -> {
                tab_today.setHintTextColor(resources.getColor(R.color.color_green))
                today_underline.setBackgroundColor(resources.getColor(R.color.color_green))
                tab_week.setHintTextColor(resources.getColor(R.color.color_black))
                week_underline.setBackgroundColor(resources.getColor(R.color.color_white))
                tab_month.setHintTextColor(resources.getColor(R.color.color_black))
                month_underline.setBackgroundColor(resources.getColor(R.color.color_white))
                progressBar.visibility=View.VISIBLE

                Handler().postDelayed({

                    showTodayList()
                    progressBar.visibility=View.GONE

                }, Constants.DELAY_TIME.toLong())

            }
            R.id.tab_week -> {
                tab_today.setHintTextColor(resources.getColor(R.color.color_black))
                today_underline.setBackgroundColor(resources.getColor(R.color.color_white))
                tab_week.setHintTextColor(resources.getColor(R.color.color_green))
                week_underline.setBackgroundColor(resources.getColor(R.color.color_green))
                tab_month.setHintTextColor(resources.getColor(R.color.color_black))
                month_underline.setBackgroundColor(resources.getColor(R.color.color_white))
                progressBar.visibility=View.VISIBLE
                Handler().postDelayed({

                    showWeeklyList()
                    progressBar.visibility=View.GONE

                }, Constants.DELAY_TIME.toLong())

            }
            R.id.tab_month -> {
                tab_today.setHintTextColor(resources.getColor(R.color.color_black))
                today_underline.setBackgroundColor(resources.getColor(R.color.color_white))
                tab_week.setHintTextColor(resources.getColor(R.color.color_black))
                week_underline.setBackgroundColor(resources.getColor(R.color.color_white))
                tab_month.setHintTextColor(resources.getColor(R.color.color_green))
                month_underline.setBackgroundColor(resources.getColor(R.color.color_green))
                progressBar.visibility=View.VISIBLE

                Handler().postDelayed({

                    progressBar.visibility=View.GONE
                    showMonthlyList()

                }, Constants.DELAY_TIME.toLong())

            }
            R.id.graph -> {

                findNavController().navigate(R.id.action_homeFragment_to_graphFragment)

            }
            R.id.home_tab -> {
                Log.e("on_click", "home_card")
                Toast.makeText(requireContext(), "Home", Toast.LENGTH_SHORT).show()
            }
            R.id.apps_tab -> {
                findNavController().navigate(R.id.action_homeFragment_to_appsFragment)
            }
            R.id.vault_tab -> {
                findNavController().navigate(R.id.action_homeFragment_to_vaultFragment)
            }
            R.id.profile_tab -> {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
        }
    }


}