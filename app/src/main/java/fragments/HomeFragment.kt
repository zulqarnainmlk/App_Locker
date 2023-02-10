package fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.lifecycle.ViewModelProviders
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
import listeners.HomeListener
import models.AppVault
import models.ListMonthlyModel
import models.ListTodayModel
import models.ListWeeklyModel
import viewmodel.DataViewModel


class HomeFragment : Fragment(), View.OnClickListener, DialogListener {
    private var packageName1: String? = null
    private var totalUsage: Int = 0

    private val DPM_ACTIVATION_REQUEST_CODE = 6135

    private val vaultDatabase by lazy { VaultDatabase.getDatabase(requireContext()).vaultDao() }
    private val todayList by lazy {
        VaultDatabase.getDatabase(requireContext()).todayUsageListDao()
    }

    private val weeklyList by lazy {
        VaultDatabase.getDatabase(requireContext()).weeklyUsageListDao()
    }
    private val monthlyList by lazy {
        VaultDatabase.getDatabase(requireContext()).MonthlyUsageListDao()
    }

    private val TAG = "PermissionDemo"
    private val PERMISSION_APPUSAGE_REQUEST_CODE = 101
    private val PERMISSION_ACCESSIBILITY_REQUEST_CODE = 102

    companion object {
        const val LAUNCH_SETTINGS_ACTIVITY = 1
    }

    private var view_Model: DataViewModel? = null
    private lateinit var homeListener: HomeListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    override fun onResume() {
        super.onResume()
        homeListener.onHomeDataChangeListener(
            toolbarVisibility = true,
            backBtnVisibility = false,
            newTitle = getString(R.string.home_title)


        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //title_home.text = getString(R.string.home_frag)
        view_Model = ViewModelProviders.of(requireActivity())[DataViewModel::class.java]

//        Handler().postDelayed({
//            init()
////            progressBar.visibility=View.GONE
//        }, Constants.DELAY_TIME.toLong())

        init()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
//        view_Model!!.backResponse.observe(viewLifecycleOwner, backResponse)
        checkRunTimePermission()
        listeners()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            byDefaultDataShow()
        }
        startBackgroundService()
//        weeklyData()
//        monthlyData()
    }


    //val backResponse=Observer<Boolean>{
//    when(it)
//    {
//        true->
//        {
//            progressBar.visibility=View.GONE
//
//
//
//            val appInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                weeklyUsageList
//            } else {
//                TODO("VERSION.SDK_INT < N")
//            }
//            val mutableAppInfoList = appInfoList.toMutableList()
//            mutableAppInfoList.sortByDescending { it.timeSpent }
//            val appInfoListAdapter = UsageAdapter(this,)
//            usage_recycler!!.adapter = appInfoListAdapter
//
//
//        }
//    }
//}
    @RequiresApi(Build.VERSION_CODES.N)
    private fun byDefaultDataShow() {
        today_shape_view.visibility = View.VISIBLE
        today.setTextColor(Color.parseColor("#4530B2"))
        showTodayList()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkRunTimePermission() {


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

    override fun dialogListerData(key: String, activity: Activity) {
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
    private val todayUsageList1: List<ListTodayModel>
        private get() {
            val packageManager = requireContext().packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            val todayUsageList1: ArrayList<ListTodayModel> = ArrayList<ListTodayModel>()
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
                    if (hour == "0" && mint == "0" && sec == "0") {
                        //todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                        //Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()

                    } else {
                        todayUsageList1.add(ListTodayModel(appName, packageName, hour, mint, sec,timeSpent))
                        val data = ListTodayModel(appName, packageName, hour, mint, sec, timeSpent)
                        Log.e("time:", "added data to db" + "\n" + data)
                        lifecycleScope.launch {
                            todayList.addData(data)

                            // blockApp(position)
                        }
                    }
                }
            }

            specificAppUsage()

            timeShowInFormat(totalUsage)
            //total usage
            Log.e("specificAppUsage", timeShowInFormat(totalUsage))
            Sharepref.setString(
                requireContext(),
                Constants.TODAY_TIME_MINUTES,
                totalMinutesToday(totalUsage).toString()
            )
            Sharepref.setString(
                requireContext(),
                Constants.TODAY_TOTAL_TIME,
                timeShowInFormat(totalUsage)
            )
            ///////////////////////////////////////////////////////////////////////////////
            totalMinutesToday(totalUsage)
/////////////////////////////////////////////////////////////////////////////////////////////// time today in format/////////////////////////////////////////
            total_time.text = timeShowInFormat(totalUsage)
            return todayUsageList1
        }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun todayTimeSpent(packageName: String): Int {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val beginTime = calendar.timeInMillis
        val endTime = beginTime + Utils.DAY_IN_MILLIS

        val appUsageMap = Utils.getTimeSpent(requireContext(), packageName, beginTime, endTime)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun showTodayList() {
        usage_recycler.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch {
            todayList.getData().collect { data ->
                if (data.isNotEmpty()) {
                    val todayUsageList: ArrayList<ListTodayModel> = ArrayList<ListTodayModel>()
//                        Log.e("time:", data.toString())
                    todayUsageList.clear()
                    Log.e(
                        "time:",
                        "-------------------------------------------------------DB DATA------------------------------------- "
                    )
                    todayUsageList.addAll(data.sortedByDescending { it.timeSpent })
//                        Log.e("time:", todayUsageList.toString())
                    val appInfoListAdapter = UsageAdapter(requireContext(), todayUsageList)
                    usage_recycler!!.adapter = appInfoListAdapter
                    appInfoListAdapter.notifyDataSetChanged()

                    /////adding data to db///////////
                    Handler().postDelayed({
                        Log.e(
                            "time:",
                            todayUsageList1.toString()
                        )
                        val appInfoList = todayUsageList1
                        val mutableAppInfoList = appInfoList.toMutableList()
                        mutableAppInfoList.sortByDescending { it.timeSpent }
                        val appInfoListAdapter = UsageAdapter(
                            requireContext(),
                            mutableAppInfoList as ArrayList<ListTodayModel>
                        )
                        usage_recycler!!.adapter = appInfoListAdapter
                        appInfoListAdapter.notifyDataSetChanged()
                        Log.e(
                            "time:",
                            "-------------------------------------------------------DB UPDATE------------------------------------- "
                        )
                    }, Constants.LOAD_TIME.toLong())


                } else {
                    val appInfoList = todayUsageList1
                    if (appInfoList.isNotEmpty()) {
                        val mutableAppInfoList = appInfoList.toMutableList()
                        Log.e(
                            "time:",
                            "-------------------------------------------------------FETCHED DATA------------------------------------- "
                        )
                        mutableAppInfoList.sortByDescending { it.timeSpent }
                        val appInfoListAdapter = UsageAdapter(
                            requireContext(),
                            mutableAppInfoList as ArrayList<ListTodayModel>
                        )
                        usage_recycler!!.adapter = appInfoListAdapter
                    }
                }
            }
        }

    }

    // val appInfoList = todayUsageList1
//        appInfoList.sortedByDescending { it.timeSpent }
//
//        val mutableAppInfoList = appInfoList.toMutableList()
//        mutableAppInfoList.sortByDescending { it.timeSpent }
//        val appInfoListAdapter = UsageAdapter(mutableAppInfoList as ArrayList<ListTodayModel>)
//        usage_recycler!!.adapter = appInfoListAdapter


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

        var hour = timesAllowed[0].toString()
        var mint = timesAllowed[1].toString()
        var sec = timesAllowed[2].toString()
        var totalMinutes = hour.toInt() * 60 + mint.toInt()
        //timeConvertor(hour,mint)
        return totalMinutes
        //": ${sec}s"

    }

    @get:RequiresApi(api = Build.VERSION_CODES.N)
    private val weeklyUsageList: List<ListWeeklyModel>
        private get() {
            val packageManager = requireContext().packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            val weeklyUsageList: ArrayList<ListWeeklyModel> = ArrayList<ListWeeklyModel>()
            totalUsage = 0

            for (i in packageInfoList.indices) {
                val packageInfo = packageInfoList[i]
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val packageName = packageInfo.packageName


                    val timeSpent = weeklyTimeSpent(packageName)
                    //val timeSpent1 = yesterdayTimeSpent(packageName)


                    totalUsageTime(timeSpent)
                    ////////////////////////////////////////////////////////////////////////////////
                    // totalUsageYesterDay(timeSpent1)
                    timeShowInFormat(totalUsage)
                    //totalMinutesYesterday(totalUsage1)


                    // Log.e("totalUsageyesterday",  totalMinutesYesterday(totalUsage1).toString())
                    /////////////////////////////////////////////////////////////////
                    val timesAllowed = reverseProcessTime(timeSpent)

                    val hour = timesAllowed[0].toString()
                    val mint = timesAllowed[1].toString()
                    val sec = timesAllowed[2].toString()
                    if (hour == "0" && mint == "0" && sec == "0") {
                        //todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                        //Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()

                    } else {
                         weeklyUsageList.add(ListWeeklyModel(appName, packageName, hour, mint, sec,timeSpent))
                        val data = ListWeeklyModel(appName, packageName, hour, mint, sec, timeSpent)
                        Log.e("data:", "weekly_usage_list" + "\n" + data)
                        lifecycleScope.launch {
                            weeklyList.addData(data)

                            // blockApp(position)
                        }
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

        val endCal = Calendar.getInstance()
        val appUsageMap = Utils.getTimeSpent(
            requireContext(),
            packageName,
            beginCal.timeInMillis,
            endCal.timeInMillis
        )
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showWeeklyList() {
//        view_Model!!.backResponse.value=true
        usage_recycler.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch {
            weeklyList.getData().collect { data ->
                if (data.isNotEmpty()) {
                    val weeklyUsageList: ArrayList<ListWeeklyModel> = ArrayList<ListWeeklyModel>()
//                        Log.e("time:", data.toString())
                    weeklyUsageList.clear()

                    Log.e(
                        "time:",
                        "-------------------------------------------------------DB DATA------------------------------------- "
                    )

                    weeklyUsageList.addAll(data.sortedByDescending { it.timeSpent })
//                        Log.e("time:", todayUsageList.toString())
                    val appInfoListAdapter = WeeklyUsageAdapter(requireContext(), weeklyUsageList)
                    usage_recycler!!.adapter = appInfoListAdapter
                    ///////adding data to db/////////
                    Handler().postDelayed({
                        val appInfoList = weeklyUsageList
                        val mutableAppInfoList = appInfoList.toMutableList()
                        mutableAppInfoList.sortByDescending { it.timeSpent }

                    }, Constants.LOAD_TIME.toLong())

                } else {
                    val appInfoList = weeklyUsageList
                    if (appInfoList.isNotEmpty()) {
                        val mutableAppInfoList = appInfoList.toMutableList()
                        Log.e(
                            "time:",
                            "-------------------------------------------------------FETCHED DATA------------------------------------- "
                        )
                        mutableAppInfoList.sortByDescending { it.timeSpent }
                        val appInfoListAdapter = WeeklyUsageAdapter(
                            requireContext(),
                            mutableAppInfoList as ArrayList<ListWeeklyModel>
                        )
                        usage_recycler!!.adapter = appInfoListAdapter
                    }
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun showMonthlyList() {
        usage_recycler.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch {
            monthlyList.getData().collect { data ->
                if (data.isNotEmpty()) {
                    val monthlyUsageList: ArrayList<ListMonthlyModel> =
                        ArrayList<ListMonthlyModel>()
//                        Log.e("time:", data.toString())
                    monthlyUsageList.clear()

                    Log.e(
                        "time:",
                        "-------------------------------------------------------DB DATA------------------------------------- "
                    )

                    monthlyUsageList.addAll(data.sortedByDescending { it.timeSpent })
//                        Log.e("time:", todayUsageList.toString())
                    val appInfoListAdapter = MonthlyUsageAdapter(requireContext(), monthlyUsageList)
                    usage_recycler!!.adapter = appInfoListAdapter
///////////////////////////////adding data to db///////////////////////////
                    Handler().postDelayed({

                        val appInfoList = monthlyUsageList
                        val mutableAppInfoList = appInfoList.toMutableList()
                        mutableAppInfoList.sortByDescending { it.timeSpent }
                        val appInfoListAdapter = MonthlyUsageAdapter(
                            requireContext(),
                            mutableAppInfoList as ArrayList<ListMonthlyModel>
                        )
                        usage_recycler!!.adapter = appInfoListAdapter

                    }, Constants.LOAD_TIME.toLong())

                } else {
                    val appInfoList = monthlyUsageList
                    if (appInfoList.isNotEmpty()) {
                        val mutableAppInfoList = appInfoList.toMutableList()
                        Log.e(
                            "time:",
                            "-------------------------------------------------------FETCHED DATA------------------------------------- "
                        )
                        mutableAppInfoList.sortByDescending { it.timeSpent }
                        val appInfoListAdapter = MonthlyUsageAdapter(
                            requireContext(),
                            mutableAppInfoList as ArrayList<ListMonthlyModel>
                        )
                        usage_recycler!!.adapter = appInfoListAdapter
                    }
                }
            }
        }

//    usage_recycler.layoutManager = LinearLayoutManager(requireActivity())
//
//    val appInfoList = monthlyUsageList
//    val mutableAppInfoList = appInfoList.toMutableList()
//    mutableAppInfoList.sortByDescending { it.timeSpent }
        // val appInfoListAdapter = UsageAdapter(mutableAppInfoList as ArrayList<AppInfoModel>)
        //usage_recycler!!.adapter = appInfoListAdapter
    }

    private val monthlyUsageList: List<ListMonthlyModel>
        @RequiresApi(Build.VERSION_CODES.N)
        private get() {
            val packageManager = requireContext().packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            val monthlyUsageList: ArrayList<ListMonthlyModel> = ArrayList<ListMonthlyModel>()
            totalUsage = 0
            for (i in packageInfoList.indices) {
                val packageInfo = packageInfoList[i]
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val packageName = packageInfo.packageName


                    val timeSpent = monthlyTimeSpent(packageName)

                    totalUsageTime(timeSpent)

                    val timesAllowed = reverseProcessTime(timeSpent)

                    val hour = timesAllowed[0].toString()
                    val mint = timesAllowed[1].toString()
                    val sec = timesAllowed[2].toString()
                    if (hour == "0" && mint == "0" && sec == "0") {
//                        todayUsageList.add(AppInfoModel(appName, appIcon, packageName, hour, mint, sec,timeSpent))
                        //Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()

                    } else {
                        monthlyUsageList.add(ListMonthlyModel(appName, packageName, hour, mint, sec,timeSpent))
                        val data =
                            ListMonthlyModel(appName, packageName, hour, mint, sec, timeSpent)
                        Log.e("data:", "weekly_usage_list" + "\n" + data)
                        lifecycleScope.launch {
                            monthlyList.addData(data)

                            // blockApp(position)
                        }
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


        val appUsageMap = Utils.getTimeSpent(
            requireContext(),
            packageName,
            beginCal.timeInMillis,
            endCal.timeInMillis
        )
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }

    private fun listeners() {
        today.setOnClickListener(this)
        sevenDays.setOnClickListener(this)
        month.setOnClickListener(this)
        graph_icon.setOnClickListener(this)
        //home_tab.setOnClickListener(this)
        all_apps.setOnClickListener(this)
        vault.setOnClickListener(this)
        profile.setOnClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.today -> {

                today_shape_view.visibility = View.VISIBLE
                today.setTextColor(Color.parseColor("#4530B2"))
                seven_days_shape_view.visibility = View.GONE
                sevenDays.setTextColor(Color.parseColor("#FFFFFFFF"))
                month_shape_view.visibility = View.GONE
                month.setTextColor(Color.parseColor("#FFFFFFFF"))
//                   progressBar.visibility=View.VISIBLE
                showTodayList()

//                   Handler().postDelayed({
//
//                       showTodayList()
////                       progressBar.visibility=View.GONE
//
//                   }, Constants.DELAY_TIME.toLong())


            }
            R.id.sevenDays -> {
//
                today_shape_view.visibility = View.GONE
                today.setTextColor(Color.parseColor("#FFFFFFFF"))
                seven_days_shape_view.visibility = View.VISIBLE
                sevenDays.setTextColor(Color.parseColor("#4530B2"))
                month_shape_view.visibility = View.GONE
                month.setTextColor(Color.parseColor("#FFFFFFFF"))
                //progressBar.visibility=View.VISIBLE
//                    Handler().postDelayed({
//                        showWeeklyList()
//                    }, Constants.DELAY_TIME.toLong())
                showWeeklyList()


            }
            R.id.month -> {

                today_shape_view.visibility = View.GONE
                today.setTextColor(Color.parseColor("#FFFFFFFF"))
                seven_days_shape_view.visibility = View.GONE
                sevenDays.setTextColor(Color.parseColor("#FFFFFFFF"))
                month_shape_view.visibility = View.VISIBLE
                month.setTextColor(Color.parseColor("#4530B2"))
                // progressBar.visibility=View.VISIBLE

//                    Handler().postDelayed({
//
//                        //showMonthlyList()
//                        progressBar.visibility=View.GONE
//
//                    }, Constants.DELAY_TIME.toLong())
                showMonthlyList()


            }
            R.id.graph_icon -> {

                findNavController().navigate(R.id.action_homeFragment_to_graphFragment)

            }
            R.id.all_apps -> {
                findNavController().navigate(R.id.action_homeFragment_to_appsFragment)
            }
            R.id.vault -> {
                findNavController().navigate(R.id.action_homeFragment_to_vaultFragment)
            }
            R.id.profile -> {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
        }
    }


}