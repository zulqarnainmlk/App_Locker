package fragments

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_graph.*
import kotlinx.android.synthetic.main.fragment_graph.month
import kotlinx.android.synthetic.main.fragment_graph.month_shape_view
import kotlinx.android.synthetic.main.fragment_graph.progressBar
import kotlinx.android.synthetic.main.fragment_graph.sevenDays
import kotlinx.android.synthetic.main.fragment_graph.seven_days_shape_view
import kotlinx.android.synthetic.main.fragment_graph.today
import kotlinx.android.synthetic.main.fragment_graph.today_shape_view
import kotlinx.android.synthetic.main.fragment_graph.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import listeners.HomeListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class GraphFragment : Fragment(),View.OnClickListener {
    var counter = 0
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
    private lateinit var homeListener: HomeListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }
    override fun onResume() {
        super.onResume()
        homeListener.onHomeDataChangeListener(
            toolbarVisibility = true,
            backBtnVisibility = true,
            newTitle = "Statistics"


        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun init() {
        listeners()
        byDefaultGraph()


    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun byDefaultGraph() {
        today_shape_view.visibility=View.VISIBLE
        today.setTextColor(Color.parseColor("#4530B2"))
        todayGraphView()
        progressBar.visibility=View.GONE
    }
    private fun todayGraphView() {
        today_percentage.visibility=View.VISIBLE
        remain_percentage.visibility=View.VISIBLE
        weekly_today.visibility=View.GONE
        weekly_previous.visibility=View.GONE
        weekly_two_days_ago.visibility=View.GONE
        weekly_three_days_ago.visibility=View.GONE
        weekly_four_days_ago.visibility=View.GONE
        weekly_five_days_ago.visibility=View.GONE
        weekly_six_days_ago.visibility=View.GONE
        pieChart.setUsePercentValues(false)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
//        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 88f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        val today_Total_Time= Sharepref.getString(requireContext(),Constants.TODAY_TOTAL_TIME,"")
        pieChart.setDrawCenterText(true)
        pieChart.centerText=today_Total_Time
        pieChart.setCenterTextSize(30f)
        pieChart.setCenterTextColor(Color.parseColor("#4530B2"))
        pieChart.setHoleColor(Color.parseColor("#D8E3EB"))

        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        //hiding label for slice

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        val today_Time= Sharepref.getString(requireContext(),Constants.TODAY_TIME_MINUTES,"")
       // val today_Total_Time= Sharepref.getString(requireContext(),Constants.TODAY_TOTAL_TIME,"")
        val entries: ArrayList<PieEntry> = ArrayList()
        val today_percent=today_Time!!.toFloat()/1440 *100
        entries.add(PieEntry(today_percent))
            //today_Total_Time))
        entries.add(PieEntry(100-today_percent))
//        entries.add(PieEntry(0f))
//        entries.add(PieEntry(0f))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")
        /////set values to textView
        appusage.Utils.roundOffDecimal(today_percent.toDouble())
        val circleText1= appusage.Utils.roundOffDecimal(today_percent.toDouble())
        circle1Text.text= buildString {
        append(circleText1.toString())
        append("%")
    }
        val remaining=100-today_percent
        appusage.Utils.roundOffDecimal(remaining.toDouble())
        val circleText2= appusage.Utils.roundOffDecimal(remaining.toDouble())
        circle2Text.text= buildString {
        append(circleText2.toString())
        append("%")

    }


        // on below line we are setting icons.
            dataSet.setDrawIcons(false).toString()

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple))
        colors.add(resources.getColor(R.color.teal_200))
        colors.add(resources.getColor(R.color.red))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setDrawValues(false)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun weeklyGraphView() {
        today_percentage.visibility=View.GONE
        remain_percentage.visibility=View.GONE
        weekly_today.visibility=View.VISIBLE
        weekly_previous.visibility=View.VISIBLE
        weekly_two_days_ago.visibility=View.VISIBLE
        weekly_three_days_ago.visibility=View.VISIBLE
        weekly_four_days_ago.visibility=View.VISIBLE
        weekly_five_days_ago.visibility=View.VISIBLE
        weekly_six_days_ago.visibility=View.VISIBLE
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
//        pieChart.setHoleColor(Color.WHITE)


        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
      //  pieChart.holeRadius = 58f
        pieChart.holeRadius = 88f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawEntryLabels(false)

        // on below line we are setting center text
        val weekly_Total_Time= Sharepref.getString(requireContext(),Constants.WEEKLY_TOTAL_TIME,"")
        pieChart.setDrawCenterText(true)
        pieChart.centerText=weekly_Total_Time
        pieChart.setCenterTextSize(30f)
        pieChart.setCenterTextColor(Color.parseColor("#4530B2"))
        pieChart.setHoleColor(Color.parseColor("#D8E3EB"))
        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.legend.formSize =18f
        pieChart.legend.textSize=15f
        val l: Legend = pieChart.legend // get legend of pie
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER;
        l.orientation = Legend.LegendOrientation.HORIZONTAL;
        l.isWordWrapEnabled = true;
//        l.verticalAlignment =
//            Legend.LegendVerticalAlignment.CENTER // set vertical alignment for legend
//
//        l.horizontalAlignment =
//            Legend.LegendHorizontalAlignment.RIGHT // set horizontal alignment for legend
//
//        l.orientation = Legend.LegendOrientation.VERTICAL // set orientation for legend

        l.setDrawInside(false)
//        pieChart.setEntryLabelColor(Color.WHITE)
//        pieChart.setEntryLabelTextSize(12f)

        val today_Time= Sharepref.getString(requireContext(),Constants.TODAY_TIME_MINUTES,"")
        val aDayAgoMinutes=Sharepref.getString(requireContext(),Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        val twoDaysAgoMinutes=Sharepref.getString(requireContext(),Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        val threeDaysAgoMinutes=Sharepref.getString(requireContext(),Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        val fourDaysAgoMinutes=  Sharepref.getString(requireContext(),Constants.FOUR_DAYS_TIME_MINUTES, "")!!.toInt()
        val fiveDaysAgoMinutes=  Sharepref.getString(requireContext(),Constants.FIVE_DAYS_TIME_MINUTES, "")!!.toInt()
        val sixDaysAgoMinutes=  Sharepref.getString(requireContext(),Constants.SIX_DAYS_TIME_MINUTES, "")!!.toInt()
        val sevenDaysAgoMinutes=  Sharepref.getString(requireContext(),Constants.SEVEN_DAYS_TIME_MINUTES, "")!!.toInt()

        // val today_Total_Time= Sharepref.getString(requireContext(),Constants.TODAY_TOTAL_TIME,"")
        val entries: ArrayList<PieEntry> = ArrayList()
        val ago_percent=aDayAgoMinutes!!.toFloat()/1440 *100
        val two_days_ago_percent=twoDaysAgoMinutes!!.toFloat()/1440 *100
        val three_days_ago_percent=threeDaysAgoMinutes!!.toFloat()/1440 *100
        val four_days_ago_percent=fourDaysAgoMinutes!!.toFloat()/1440 *100
        val five_days_ago_percent=fiveDaysAgoMinutes!!.toFloat()/1440 *100
        val six_days_ago_percent=sixDaysAgoMinutes!!.toFloat()/1440 *100
        val seven_days_ago_percent=sevenDaysAgoMinutes!!.toFloat()/1440 *100
        val remaining=100-ago_percent+two_days_ago_percent+three_days_ago_percent+four_days_ago_percent+five_days_ago_percent+six_days_ago_percent+seven_days_ago_percent
        entries.add(PieEntry(ago_percent,getCalculatedDate("", "dd-MM-yyyy", -1)))
        entries.add(PieEntry(two_days_ago_percent,getCalculatedDate("", "dd-MM-yyyy", -2)))
        entries.add(PieEntry(three_days_ago_percent,getCalculatedDate("", "dd-MM-yyyy", -3)))
        entries.add(PieEntry(four_days_ago_percent,getCalculatedDate("", "dd-MM-yyyy", -4)))
        entries.add(PieEntry(five_days_ago_percent,getCalculatedDate("", "dd-MM-yyyy", -5)))
        entries.add(PieEntry(six_days_ago_percent,getCalculatedDate("", "dd-MM-yyyy", -6)))
        entries.add(PieEntry(seven_days_ago_percent,getCalculatedDate("", "dd-MM-yyyy", -7)))
        //entries.add(PieEntry(remaining))
        //today_Total_Time))
        //entries.add(PieEntry(100-today_percent))
//        entries.add(PieEntry(0f))
//        entries.add(PieEntry(0f))
        ///////////////////////////////data set for textViews////////////////////////////////////////
        appusage.Utils.roundOffDecimal(ago_percent.toDouble())
//        val circleText1= getCalculatedDate("", "dd-MM-yyyy", -1)+"\n"+appusage.Utils.roundOffDecimal(ago_percent.toDouble())+"%"
//        circle1Text.text=circleText1
        weekly_today_text.text=getCalculatedDate("", "dd-MM-yyyy", -1)
        val  weekly_today= appusage.Utils.roundOffDecimal(ago_percent.toDouble())
        weekly_today_percent.text= buildString {
            append(weekly_today.toString())
            append("%")
        }
        weekly_previous_text.text=getCalculatedDate("", "dd-MM-yyyy", -2)
        val  weekly_previous= appusage.Utils.roundOffDecimal(two_days_ago_percent.toDouble())
        weekly_previous_percent.text= buildString {
            append(weekly_previous.toString())
            append("%")
        }
        weekly_two_days_ago_text.text=getCalculatedDate("", "dd-MM-yyyy", -3)
        val  weekly_two_days_ago= appusage.Utils.roundOffDecimal(three_days_ago_percent.toDouble())
        weekly_two_days_ago_percent.text= buildString {
            append(weekly_two_days_ago.toString())
            append("%")
        }
        weekly_three_days_ago_text.text=getCalculatedDate("", "dd-MM-yyyy", -4)
        val  weekly_three_days_ago= appusage.Utils.roundOffDecimal(four_days_ago_percent.toDouble())
        weekly_three_days_ago_percent.text= buildString {
            append(weekly_three_days_ago.toString())
            append("%")
        }
        weekly_four_days_ago_text.text=getCalculatedDate("", "dd-MM-yyyy", -5)
        val  weekly_four_days_ago= appusage.Utils.roundOffDecimal(five_days_ago_percent.toDouble())
        weekly_four_days_ago_percent.text= buildString {
            append(weekly_four_days_ago.toString())
            append("%")
        }
        weekly_five_days_ago_text.text=getCalculatedDate("", "dd-MM-yyyy", -6)
        val  weekly_five_days_ago= appusage.Utils.roundOffDecimal(six_days_ago_percent.toDouble())
        weekly_five_days_ago_percent.text= buildString {
            append(weekly_five_days_ago.toString())
            append("%")
        }
        weekly_six_days_ago_text.text=getCalculatedDate("", "dd-MM-yyyy", -7)
        val  weekly_six_days_ago= appusage.Utils.roundOffDecimal(seven_days_ago_percent.toDouble())
        weekly_six_days_ago_percent.text= buildString {
            append(weekly_six_days_ago.toString())
            append("%")
        }


        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "")
        // on below line we are setting icons.
        dataSet.setDrawIcons(false)
        dataSet.setDrawValues(false)
        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple))
        colors.add(resources.getColor(R.color.teal_200))
        colors.add(resources.getColor(R.color.orange))
        colors.add(resources.getColor(R.color.green_Shade_1))
        colors.add(resources.getColor(R.color.red))
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.teal_700))
        // colors.add(resources.getColor(R.color.light_pink_1))
        dataSet.colors = colors
        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(8f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data
        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
            }
    ////////////////////////////////weekly+monthly graph data///////////////////////////////////////////
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
        val packageManager = requireContext().packageManager
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
        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_ago:")
            aDayAgoMinutes=total-todayMinutes
            Log.e("timeInMinutes", "aDayAgoMinutes:$aDayAgoMinutes")
            Sharepref.setString(requireContext(), Constants.DAY_AGO_TIME_MINUTES, aDayAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_ago:")
            aDayAgoMinutes=todayMinutes-total
            Log.e("timeInMinutes", "yesterdayMinutes:$aDayAgoMinutes")
            Sharepref.setString(requireContext(), Constants.DAY_AGO_TIME_MINUTES, aDayAgoMinutes.toString())
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //twoDaysAgo
    @RequiresApi(Build.VERSION_CODES.N)
    private fun twoDaysAgoPackageProvider() {
        val packageManager = requireContext().packageManager
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

        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(requireContext(), Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_two_days_ago:")
            twoDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes
            Log.e("timeInMinutes", "twoDaysAgoMinutes:$twoDaysAgoMinutes")
            Sharepref.setString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES, twoDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_two_days_ago:")
            twoDaysAgoMinutes=todayMinutes-aDayAgoMinutes-total
            Log.e("timeInMinutes", "twoDaysAgoMinutes:$twoDaysAgoMinutes")
            Sharepref.setString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES, twoDaysAgoMinutes.toString())
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //three days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun threeDaysAgoPackageProvider() {
        val packageManager = requireContext().packageManager
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
        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(requireContext(), Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_three_days_ago:")
            threeDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes
            Log.e("timeInMinutes", "threeDaysAgoMinutes:$threeDaysAgoMinutes")
            Sharepref.setString(requireContext(),
                Constants.THREE_DAYS_TIME_MINUTES, threeDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_three_days_ago:")
            threeDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-total
            Log.e("timeInMinutes", "threeDaysAgoMinutes:$threeDaysAgoMinutes")
            Sharepref.setString(requireContext(),
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //four days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fourDaysAgoPackageProvider() {
        val packageManager = requireContext().packageManager
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
        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(requireContext(), Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_four_days_ago:")
            fourDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes
            Log.e("timeInMinutes", "fourDaysAgoMinutes:$fourDaysAgoMinutes")
            Sharepref.setString(requireContext(),
                Constants.FOUR_DAYS_TIME_MINUTES, fourDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_four_days_ago:")
            fourDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-total
            Log.e("timeInMinutes", "fourDaysAgoMinutes:$fourDaysAgoMinutes")
            Sharepref.setString(requireContext(),
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //five days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fiveDaysAgoPackageProvider() {
        val packageManager = requireContext().packageManager
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
        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(requireContext(), Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        fourDaysAgoMinutes=  Sharepref.getString(requireContext(),
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
            Sharepref.setString(requireContext(),
                Constants.FIVE_DAYS_TIME_MINUTES, fiveDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_five_days_ago:")
            fiveDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-total
            Log.e("timeInMinutes", "fiveDaysAgoMinutes:$fiveDaysAgoMinutes")
            Sharepref.setString(requireContext(),
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //six days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sixDaysAgoPackageProvider() {
        val packageManager = requireContext().packageManager
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
        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(requireContext(), Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        fourDaysAgoMinutes=  Sharepref.getString(requireContext(),
            Constants.FOUR_DAYS_TIME_MINUTES, "")!!.toInt()
        fiveDaysAgoMinutes=  Sharepref.getString(requireContext(),
            Constants.FIVE_DAYS_TIME_MINUTES, "")!!.toInt()
        if(total!=0)
        {
            Log.e("timeInMinutes","if_six_days_ago:")
            sixDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes
            Log.e("timeInMinutes", "sixDaysAgoMinutes:$sixDaysAgoMinutes")
            Sharepref.setString(requireContext(), Constants.SIX_DAYS_TIME_MINUTES, sixDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_six_days_ago:")
            sixDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes-total
            Log.e("timeInMinutes", "sixDaysAgoMinutes:$sixDaysAgoMinutes")
            Sharepref.setString(requireContext(), Constants.SIX_DAYS_TIME_MINUTES, sixDaysAgoMinutes.toString())
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        Log.e("weekend", endCal.timeInMillis.toString())
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    //seven days ago
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sevenDaysAgoPackageProvider() {
        val packageManager = requireContext().packageManager
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
        Sharepref.setString(requireContext(), Constants.WEEKLY_TOTAL_TIME,timeShowInFormat(totalUsage7))
        Sharepref.setString(requireContext(), Constants.WEEK4_TOTAL_MINUTES,totalMinutesToday(totalUsage7).toString())
        total=totalMinutesToday(totalUsage7)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","eightDaysTotal:"  + totalMinutesToday(totalUsage7).toString())
        //today minutes get by sharepref
        todayMinutes= Sharepref.getString(requireContext(), Constants.TODAY_TIME_MINUTES,"")!!.toInt()
        aDayAgoMinutes= Sharepref.getString(requireContext(), Constants.DAY_AGO_TIME_MINUTES,"")!!.toInt()
        twoDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.TWO_DAYS_TIME_MINUTES,"")!!.toInt()
        threeDaysAgoMinutes=
            Sharepref.getString(requireContext(), Constants.THREE_DAYS_TIME_MINUTES,"")!!.toInt()
        fourDaysAgoMinutes=  Sharepref.getString(requireContext(),
            Constants.FOUR_DAYS_TIME_MINUTES, "")!!.toInt()
        fiveDaysAgoMinutes=  Sharepref.getString(requireContext(),
            Constants.FIVE_DAYS_TIME_MINUTES, "")!!.toInt()
        sixDaysAgoMinutes=  Sharepref.getString(requireContext(), Constants.SIX_DAYS_TIME_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_seven_days_ago:")
            sevenDaysAgoMinutes=total-todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes-sixDaysAgoMinutes
            Log.e("timeInMinutes", "sevenDaysAgoMinutes:$sevenDaysAgoMinutes")
            Sharepref.setString(requireContext(),
                Constants.SEVEN_DAYS_TIME_MINUTES, sevenDaysAgoMinutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_seven_days_ago:")
            sevenDaysAgoMinutes=todayMinutes-aDayAgoMinutes-twoDaysAgoMinutes-threeDaysAgoMinutes-fourDaysAgoMinutes-fiveDaysAgoMinutes-sixDaysAgoMinutes-total
            Log.e("timeInMinutes", "sevenDaysAgoMinutes:$sevenDaysAgoMinutes")
            Sharepref.setString(requireContext(),
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
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
        val packageManager = requireContext().packageManager
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
//        Sharepref.setString(requireContext(),Constants.WEEKLY_TOTAL_TIME,timeShowInFormat(week3Usage))
        totalMinutesToday(week3Usage)
        total=totalMinutesToday(week3Usage)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","week3Total:"  + totalMinutesToday(week3Usage).toString())
        //today minutes get by sharepref

        week4Minutes=   Sharepref.getString(requireContext(), Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_week3:")
            week3Minutes=total-week4Minutes
            Log.e("timeInMinutes", "week3Minutes:$week3Minutes")
            Sharepref.setString(requireContext(), Constants.WEEK3_TOTAL_MINUTES, week3Minutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_week3:")
            week3Minutes=week4Minutes-total
            Log.e("timeInMinutes", "week3Minutes:$week3Minutes")
            Sharepref.setString(requireContext(), Constants.WEEK3_TOTAL_MINUTES, week3Minutes.toString())
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    ///////////////////////////////////////////week2////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week2PackageProvider() {
        val packageManager = requireContext().packageManager
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
//        Sharepref.setString(requireContext(),Constants.WEEKLY_TOTAL_TIME,timeShowInFormat(week3Usage))
        totalMinutesToday(week2Usage)
        total=totalMinutesToday(week2Usage)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","week2Total:"  + totalMinutesToday(week2Usage).toString())
        //today minutes get by sharepref

        week3Minutes=   Sharepref.getString(requireContext(), Constants.WEEK3_TOTAL_MINUTES, "")!!.toInt()
        week4Minutes=   Sharepref.getString(requireContext(), Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_week2:")
            week3Minutes=total-week4Minutes-week3Minutes
            Log.e("timeInMinutes", "week2Minutes:$week2Minutes")
            Sharepref.setString(requireContext(), Constants.WEEK2_TOTAL_MINUTES, week2Minutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_week2:")
            week3Minutes=week4Minutes-week3Minutes-total
            Log.e("timeInMinutes", "week2Minutes:$week2Minutes")
            Sharepref.setString(requireContext(), Constants.WEEK2_TOTAL_MINUTES, week2Minutes.toString())
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    ////////////////////////////////////////week1//////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun week1PackageProvider() {
        val packageManager = requireContext().packageManager
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
        Sharepref.setString(requireContext(), Constants.MONTHLY_TOTAL_TIME, timeShowInFormat(week1Usage))
        total=totalMinutesToday(week1Usage)
        Log.e("timeInMinutes","----------------------------------------------------------------------------------------------------------------------")
        Log.e("timeInMinutes","week1Total:"  + totalMinutesToday(week1Usage).toString())
        //today minutes get by sharepref
        week2Minutes=   Sharepref.getString(requireContext(), Constants.WEEK2_TOTAL_MINUTES, "")!!.toInt()
        week3Minutes=   Sharepref.getString(requireContext(), Constants.WEEK3_TOTAL_MINUTES, "")!!.toInt()
        week4Minutes=   Sharepref.getString(requireContext(), Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()

        if(total!=0)
        {
            Log.e("timeInMinutes","if_week1:")
            week1Minutes=total-week4Minutes-week3Minutes-week2Minutes
            Log.e("timeInMinutes", "week1Minutes:$week1Minutes")
            Sharepref.setString(requireContext(), Constants.WEEK1_TOTAL_MINUTES, week1Minutes.toString())
        }
        else
        {
            Log.e("timeInMinutes","else_week1:")
            week1Minutes=week4Minutes-week3Minutes-week2Minutes-total
            Log.e("timeInMinutes", "week1Minutes:$week1Minutes")
            Sharepref.setString(requireContext(), Constants.WEEK1_TOTAL_MINUTES, week1Minutes.toString())
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
        val appUsageMap = appusage.Utils.getTimeSpent(requireContext(), packageName, beginCal.timeInMillis, endCal.timeInMillis)
        var usageTime = appUsageMap[packageName]
        //Log.e("Tag15","total time"+ usageTime.toString())
        if (usageTime == null) usageTime = 0
        return usageTime

    }
    ///////////////////////////////////////////formation methods////////////////////////////
    private fun timeShowInFormat(timeSpent: Int): String {
        val timesAllowed = appusage.Utils.reverseProcessTime(timeSpent)
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
        val timesAllowed = appusage.Utils.reverseProcessTime(timeSpent)
        Log.e("time:",timesAllowed.toString())

        var hour = timesAllowed[0].toString()
        var mint = timesAllowed[1].toString()
        var sec = timesAllowed[2].toString()
        var totalMinutes=hour.toInt()*60+mint.toInt()
        //timeConvertor(hour,mint)
        return totalMinutes
        //": ${sec}s"

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun monthlyGraphView() {
        today_percentage.visibility=View.GONE
        remain_percentage.visibility=View.GONE
        weekly_today.visibility=View.VISIBLE
        weekly_previous.visibility=View.VISIBLE
        weekly_two_days_ago.visibility=View.VISIBLE
        weekly_three_days_ago.visibility=View.VISIBLE
        weekly_four_days_ago.visibility=View.GONE
        weekly_five_days_ago.visibility=View.GONE
        weekly_six_days_ago.visibility=View.GONE
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
//        pieChart.setHoleColor(Color.WHITE)
        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 88f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawEntryLabels(false)

        // on below line we are setting center text
        val monthly_Total_Time= Sharepref.getString(requireContext(),Constants.MONTHLY_TOTAL_TIME,"")
        pieChart.setDrawCenterText(true)
        pieChart.centerText=monthly_Total_Time
        pieChart.setCenterTextSize(30f)
        pieChart.setCenterTextColor(Color.parseColor("#4530B2"))
        pieChart.setHoleColor(Color.parseColor("#D8E3EB"))

        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.legend.formSize =18f
        pieChart.legend.textSize=15f
        val l: Legend = pieChart.legend // get legend of pie
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER;
        l.orientation = Legend.LegendOrientation.HORIZONTAL;
        l.isWordWrapEnabled = true;
//        l.verticalAlignment =
//            Legend.LegendVerticalAlignment.CENTER // set vertical alignment for legend
//
//        l.horizontalAlignment =
//            Legend.LegendHorizontalAlignment.RIGHT // set horizontal alignment for legend
//
//        l.orientation = Legend.LegendOrientation.VERTICAL // set orientation for legend


        l.setDrawInside(false)
//        pieChart.setEntryLabelColor(Color.WHITE)
//        pieChart.setEntryLabelTextSize(12f)


        val week4TotalMinutes=  Sharepref.getString(requireContext(),Constants.WEEK4_TOTAL_MINUTES, "")!!.toInt()
        val week3TotalMinutes=  Sharepref.getString(requireContext(),Constants.WEEK3_TOTAL_MINUTES, "")!!.toInt()
        val week2TotalMinutes=  Sharepref.getString(requireContext(),Constants.WEEK2_TOTAL_MINUTES, "")!!.toInt()
        val week1TotalMinutes=  Sharepref.getString(requireContext(),Constants.WEEK1_TOTAL_MINUTES, "")!!.toInt()
        Log.e("tagtime:",week2TotalMinutes.toString())
        Log.e("tagtime:", week1TotalMinutes.toString())

        // val today_Total_Time= Sharepref.getString(requireContext(),Constants.TODAY_TOTAL_TIME,"")
        val entries: ArrayList<PieEntry> = ArrayList()
        val week4Percentage= week4TotalMinutes.toFloat()/1440 *100
        val week3Percentage= week3TotalMinutes.toFloat()/1440 *100
        val week2Percentage= week2TotalMinutes.toFloat()/1440 *100
        val week1Percentage= week1TotalMinutes.toFloat()/1440 *100

        entries.add(PieEntry(week4Percentage,getString(R.string.week4)))
        entries.add(PieEntry(week3Percentage,getString(R.string.week3)))
        entries.add(PieEntry(week2Percentage,getString(R.string.week2)))
        entries.add(PieEntry(week1Percentage,getString(R.string.week1)))
        ////////////// set values for textViews
        weekly_today_text.text="week4"
        val  week_four= appusage.Utils.roundOffDecimal(week4Percentage.toDouble())
        weekly_today_percent.text= buildString {
            append(week_four.toString())
            append("%")
        }
        weekly_previous_text.text="week3"
        val  week_three= appusage.Utils.roundOffDecimal(week3Percentage.toDouble())
        weekly_previous_percent.text= buildString {
            append(week_three.toString())
            append("%")
        }
        weekly_two_days_ago_text.text="week2"
        val  week_two= appusage.Utils.roundOffDecimal(week2Percentage.toDouble())
        weekly_two_days_ago_percent.text= buildString {
            append(week_two.toString())
            append("%")
        }
        weekly_three_days_ago_text.text="week1"
        val  week_one= appusage.Utils.roundOffDecimal(week1Percentage.toDouble())
        weekly_three_days_ago_percent.text= buildString {
            append(week_one.toString())
            append("%")
        }
        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")
        // on below line we are setting icons.
        dataSet.setDrawIcons(false)
        dataSet.setDrawValues(false)
        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple))
        colors.add(resources.getColor(R.color.teal_200))
        colors.add(resources.getColor(R.color.orange))
        colors.add(resources.getColor(R.color.green_Shade_1))

        // colors.add(resources.getColor(R.color.light_pink_1))
        dataSet.colors = colors
        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(8f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data
        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }
    private fun getCalculatedDate(date: String, dateFormat: String, days: Int): String {
        val cal = java.util.Calendar.getInstance()
        val s = java.text.SimpleDateFormat(dateFormat)
        if (date.isNotEmpty()) {
            cal.time = s.parse(date)
        }
        cal.add(java.util.Calendar.DAY_OF_YEAR, days)
        //////////current date
        Log.e("date","currentdate:"+s.format(Date(cal.timeInMillis)))
        /////////current day
        val day: String = DateFormat.format("EEE", cal).toString()
        Log.e("date","currentday:"+ day)
        ///////////////////////////////////////////////////////////////////////
        val mDate = s.parse(s.format(Date(cal.timeInMillis)))
        val timeInMilliseconds = mDate.time
        Log.e("date", "currentdaymilliseconds:$timeInMilliseconds")
        return day
    }

    private fun listeners() {
        today.setOnClickListener(this)
        sevenDays.setOnClickListener(this)
        month.setOnClickListener(this)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.today -> {
                today_shape_view.visibility=View.VISIBLE
                today.setTextColor(Color.parseColor("#4530B2"))
                seven_days_shape_view.visibility=View.GONE
                sevenDays.setTextColor(Color.parseColor("#FFFFFFFF"))
                month_shape_view.visibility=View.GONE
                month.setTextColor(Color.parseColor("#FFFFFFFF"))
                progressBar.visibility=View.VISIBLE

                Handler().postDelayed({

                    todayGraphView()
                    progressBar.visibility=View.GONE

                }, Constants.DELAY_TIME.toLong())
            }
            R.id.sevenDays -> {
                today_shape_view.visibility=View.GONE
                today.setTextColor(Color.parseColor("#FFFFFFFF"))
                seven_days_shape_view.visibility=View.VISIBLE
                sevenDays.setTextColor(Color.parseColor("#4530B2"))
                month_shape_view.visibility=View.GONE
                month.setTextColor(Color.parseColor("#FFFFFFFF"))
                progressBar.visibility=View.VISIBLE

                Handler().postDelayed({

                    weeklyData()
                    weeklyGraphView()
                    progressBar.visibility=View.GONE

                }, Constants.DELAY_TIME.toLong())
            }
            R.id.month -> {
                today_shape_view.visibility=View.GONE
                today.setTextColor(Color.parseColor("#FFFFFFFF"))
                seven_days_shape_view.visibility=View.GONE
                sevenDays.setTextColor(Color.parseColor("#FFFFFFFF"))
                month_shape_view.visibility=View.VISIBLE
                month.setTextColor(Color.parseColor("#4530B2"))
                progressBar.visibility=View.VISIBLE

                Handler().postDelayed({

                    monthlyData()
                    monthlyGraphView()
                    progressBar.visibility=View.GONE


                }, Constants.DELAY_TIME.toLong())


            }
        }
    }




}