package fragments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
        pieChart.setUsePercentValues(false)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        val today_Total_Time= Sharepref.getString(requireContext(),Constants.TODAY_TOTAL_TIME,"")
        pieChart.setDrawCenterText(true)
        pieChart.centerText=today_Total_Time
        pieChart.setCenterTextSize(20f)
        pieChart.setCenterTextColor(Color.BLACK)

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
        //        customCircle3.visibility=View.GONE
//        circle3Text.visibility=View.GONE
//        customCircle4.visibility=View.GONE
//        circle4Text.visibility=View.GONE
//        customCircle5.visibility=View.GONE
//        circle5Text.visibility=View.GONE
//        customCircle6.visibility=View.GONE
//        circle6Text.visibility=View.GONE
//        customCircle7.visibility=View.GONE
//        circle7Text.visibility=View.GONE

        // on below line we are setting icons.
    }
//        customCircle3.visibility=View.GONE
//        circle3Text.visibility=View.GONE
//        customCircle4.visibility=View.GONE
//        circle4Text.visibility=View.GONE
//        customCircle5.visibility=View.GONE
//        circle5Text.visibility=View.GONE
//        customCircle6.visibility=View.GONE
//        circle6Text.visibility=View.GONE
//        customCircle7.visibility=View.GONE
//        circle7Text.visibility=View.GONE

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
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawEntryLabels(false)

        // on below line we are setting center text
        val weekly_Total_Time= Sharepref.getString(requireContext(),Constants.WEEKLY_TOTAL_TIME,"")
        pieChart.setDrawCenterText(true)
        pieChart.centerText=weekly_Total_Time
        pieChart.setCenterTextSize(20f)
        pieChart.setCenterTextColor(Color.BLACK)

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
        val circleText1= getCalculatedDate("", "dd-MM-yyyy", -1)+"\n"+appusage.Utils.roundOffDecimal(ago_percent.toDouble())+"%"
        circle1Text.text=circleText1
        appusage.Utils.roundOffDecimal(remaining.toDouble())
        val circleText2= getCalculatedDate("", "dd-MM-yyyy", -2)+"\n"+ appusage.Utils.roundOffDecimal(two_days_ago_percent.toDouble())+"%"
        circle2Text.text=circleText2
//        customCircle3.visibility=View.VISIBLE
//        circle3Text.visibility=View.VISIBLE
        val circleText3=getCalculatedDate("", "dd-MM-yyyy", -3)+"\n"+ appusage.Utils.roundOffDecimal(three_days_ago_percent.toDouble())+"%"
//        circle3Text.text=circleText3
//        customCircle4.visibility=View.VISIBLE
//        circle4Text.visibility=View.VISIBLE
        val circleText4=getCalculatedDate("", "dd-MM-yyyy", -4)+"\n"+ appusage.Utils.roundOffDecimal(four_days_ago_percent.toDouble())+"%"
//        circle4Text.text=circleText4
//        customCircle5.visibility=View.VISIBLE
//        circle5Text.visibility=View.VISIBLE
        val circleText5=getCalculatedDate("", "dd-MM-yyyy", -5)+"\n"+ appusage.Utils.roundOffDecimal(five_days_ago_percent.toDouble())+"%"
//        circle5Text.text=circleText5
//
//        customCircle6.visibility=View.VISIBLE
//        circle6Text.visibility=View.VISIBLE
        val circleText6=getCalculatedDate("", "dd-MM-yyyy", -6)+"\n"+ appusage.Utils.roundOffDecimal(six_days_ago_percent.toDouble())+"%"
//        circle6Text.text=circleText6
//
//        customCircle7.visibility=View.VISIBLE
//        circle7Text.visibility=View.VISIBLE
        val circleText7=getCalculatedDate("", "dd-MM-yyyy", -7)+"\n"+ appusage.Utils.roundOffDecimal(seven_days_ago_percent.toDouble())+"%"
//        circle7Text.text=circleText7



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
    private fun monthlyGraphView() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawEntryLabels(false)

        // on below line we are setting center text
        val monthly_Total_Time= Sharepref.getString(requireContext(),Constants.MONTHLY_TOTAL_TIME,"")
        pieChart.setDrawCenterText(true)
        pieChart.centerText=monthly_Total_Time
        pieChart.setCenterTextSize(20f)
        pieChart.setCenterTextColor(Color.BLACK)

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
        appusage.Utils.roundOffDecimal(week4Percentage.toDouble())
        val circleText1= "week4"+"\n"+appusage.Utils.roundOffDecimal(week4Percentage.toDouble())+"%"
        circle1Text.text=circleText1
        appusage.Utils.roundOffDecimal(week3Percentage.toDouble())
        val circleText2= "week3"+"\n"+ appusage.Utils.roundOffDecimal(week3Percentage.toDouble())+"%"
        circle2Text.text=circleText2
//        customCircle3.visibility=View.VISIBLE
//        circle3Text.visibility=View.VISIBLE
        val circleText3="week2"+"\n"+ appusage.Utils.roundOffDecimal(week2Percentage.toDouble())+"%"
//        circle3Text.text=circleText3
//        customCircle4.visibility=View.VISIBLE
//        circle4Text.visibility=View.VISIBLE
        val circleText4="week1"+"\n"+ appusage.Utils.roundOffDecimal(week1Percentage.toDouble())+"%"
//        circle4Text.text=circleText4
//        customCircle5.visibility=View.GONE
//        circle5Text.visibility=View.GONE
//        customCircle6.visibility=View.GONE
//        circle6Text.visibility=View.GONE
//        customCircle7.visibility=View.GONE
//        circle7Text.visibility=View.GONE


        //entries.add(PieEntry(remaining))
        //today_Total_Time))
        //entries.add(PieEntry(100-today_percent))
//        entries.add(PieEntry(0f))
//        entries.add(PieEntry(0f))

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

                    progressBar.visibility=View.GONE
                    monthlyGraphView()

                }, Constants.DELAY_TIME.toLong())


            }
        }
    }




}