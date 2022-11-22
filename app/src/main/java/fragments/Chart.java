package fragments;

import static androidx.navigation.Navigation.findNavController;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_locker.R;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.HashMap;

import appusage.Utils;
import helper.Constants;
import helper.Sharepref;


public class Chart extends Fragment {

    private String packageName;
    private String appName;
    private BarChart barChart;
    private TextView appNameView;
    private TextView title;
    private View view_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart2, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        packageName = Sharepref.getString(requireContext(), Constants.CURRENT_PACKAGE_NAME,"");
        appName = Sharepref.getString(requireContext(), Constants.CURRENT_APP_NAME,"");
        Log.e("abc", packageName+":"+appName);
        title=view.findViewById(R.id.title);
        view_back=view.findViewById(R.id.view_back);
        barChart = view.findViewById(R.id.barchart);
        appNameView =view.findViewById(R.id.chart_app_name);
        title.setText(R.string.chart);
        appNameView.setText(appName);

        ArrayList<BarEntry> entries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long beginTime = calendar.getTimeInMillis();
        long endTime = beginTime + Utils.DAY_IN_MILLIS;
        for (int daysAgo = 0; daysAgo < 7; daysAgo++) {
            HashMap<String, Integer> appUsageMap = Utils.getTimeSpent(requireContext(), packageName, beginTime, endTime);
            Integer usageTime = appUsageMap.get(packageName);


            if (usageTime == null) usageTime = 0;
           // logger.log(Level.INFO, "Display message");
            Log.e("abc", usageTime.toString());
            entries.add(new BarEntry(usageTime, 7 - daysAgo - 1));
            beginTime -= Utils.DAY_IN_MILLIS;
            endTime -= Utils.DAY_IN_MILLIS;
        }
        BarDataSet bardataset = new BarDataSet(entries, "daily usage");

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<String> labels1 = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -6);
        for (int i = 0; i < 7; i++) {
            labels.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data);
        barChart.setDescription(null);
        barChart.getData().setValueFormatter(new TimeFormatter());
        barChart.getXAxis().setLabelsToSkip(0);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.animateY(500);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);


        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chart_to_homeFragment);

            }
        });






    }
    private static class TimeFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(value == 0) {
                return "0s";
            }
            int[] usageTimes = Utils.reverseProcessTime((int)value);
            String hour = String.valueOf(usageTimes[0]);
            String minute = String.valueOf(usageTimes[1]);
            String second = String.valueOf(usageTimes[2]);
            String time = "";
            if(usageTimes[0] > 0) time += hour + "h ";
            if(usageTimes[1] > 0) time += minute + "m ";
            if(usageTimes[2] > 0) time += second + "s";
            return time;
        }
    }
}