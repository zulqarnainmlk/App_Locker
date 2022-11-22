package appusage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_locker.R;

import java.util.List;

public class AppInfoListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<AppInfoModel> appInfoList;

    public AppInfoListAdapter(Context context, List<AppInfoModel> appInfoList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.appInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public AppInfoModel getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.app_list, parent, false);

            listViewHolder.appName = convertView.findViewById(R.id.list_app_name);
            listViewHolder.appIcon = convertView.findViewById(R.id.list_app_icon);
            listViewHolder.hour = convertView.findViewById(R.id.hour);
            listViewHolder.mint = convertView.findViewById(R.id.minute);
            listViewHolder.sec = convertView.findViewById(R.id.second);
//            listViewHolder.isUsageExceeded = convertView.findViewById(R.id.isUsageExceeded);
//            listViewHolder.isTracked = convertView.findViewById(R.id.isTracked);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.appName.setText(appInfoList.get(position).getAppName());
        listViewHolder.appIcon.setImageDrawable(appInfoList.get(position).getIcon());
        Log.e("abc", appInfoList.get(position).getPackageName());
        if (listViewHolder.hour.getText() == "0" && listViewHolder.mint.getText() == "0" && listViewHolder.sec.getText() == "0") {
            listViewHolder.hour.setVisibility(View.GONE);
            listViewHolder.text_hr.setVisibility(View.GONE);
            listViewHolder.mint.setVisibility(View.GONE);
            listViewHolder.text_min.setVisibility(View.GONE);
            //listViewHolder.sec.setVisibility(View.GONE);
           // listViewHolder.text_sec.setVisibility(View.GONE);
        }
        listViewHolder.hour.setText(appInfoList.get(position).getHour());
        listViewHolder.mint.setText(appInfoList.get(position).getMint());
        listViewHolder.sec.setText(appInfoList.get(position).getSec());
//        if(appInfoList.get(position).getIsUsageExceeded())
//            listViewHolder.isUsageExceeded.setVisibility(View.VISIBLE);
//        else listViewHolder.isUsageExceeded.setVisibility(View.INVISIBLE);
//        if(appInfoList.get(position).getIsTracked())
//            listViewHolder.isTracked.setVisibility(View.VISIBLE);
//        else listViewHolder.isTracked.setVisibility(View.INVISIBLE);

        return convertView;
    }

    class ViewHolder {
        TextView appName;
        ImageView appIcon;
        TextView hour;
        TextView mint;
        TextView sec;
        TextView text_hr;
        TextView text_min;
        TextView text_sec;

    }
}
