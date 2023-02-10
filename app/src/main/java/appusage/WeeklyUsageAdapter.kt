package appusage

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_locker.R
import models.ListTodayModel
import models.ListWeeklyModel
import java.security.AccessController.getContext


class WeeklyUsageAdapter(private val context: Context, private var mList: ArrayList<ListWeeklyModel>) : RecyclerView.Adapter<WeeklyUsageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyUsageAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_list, parent, false)

        return WeeklyUsageAdapter.ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: WeeklyUsageAdapter.ViewHolder, position: Int) {

        val AppInfo = mList[position]

        // sets the image to the imageview from our itemHolder class
        // sets the text to the textview from our itemHolder class
        holder.appName.text = AppInfo.appName
        try {
            val icon: Drawable =
                context.packageManager.getApplicationIcon(AppInfo.packageName)
            holder.appIcon.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        holder.hour.text = AppInfo.hour
        holder.mint.text = AppInfo.mint
        holder.sec.text = AppInfo.sec
        if(AppInfo.hour=="0" && AppInfo.mint=="0" && AppInfo.sec=="0")
        {
            holder.usage_list.visibility=View.GONE

        }
        else if(AppInfo.hour!="0" && AppInfo.mint=="0" && AppInfo.sec=="0")
        {
            holder.hour.visibility=View.VISIBLE
            holder.text_hr.visibility=View.VISIBLE
            holder.mint.visibility=View.VISIBLE
            holder.text_min.visibility=View.VISIBLE
            holder.sec.visibility=View.GONE
            holder.text_sec.visibility=View.GONE
        }
        else if(AppInfo.hour=="0" && AppInfo.mint!="0" && AppInfo.sec=="0")
        {
            holder.hour.visibility=View.GONE
            holder.text_hr.visibility=View.GONE
            holder.mint.visibility=View.VISIBLE
            holder.text_min.visibility=View.VISIBLE
            holder.sec.visibility=View.GONE
            holder.text_sec.visibility=View.GONE
        }
        else if(AppInfo.hour!="0" && AppInfo.mint!="0" && AppInfo.sec!="0")
        {
            holder.hour.visibility=View.VISIBLE
            holder.text_hr.visibility=View.VISIBLE
            holder.mint.visibility=View.VISIBLE
            holder.text_min.visibility=View.VISIBLE
            holder.sec.visibility=View.GONE
            holder.text_sec.visibility=View.GONE
        }
        else if(AppInfo.hour=="0" && AppInfo.mint!="0" && AppInfo.sec!="0")
        {
            holder.hour.visibility=View.GONE
            holder.text_hr.visibility=View.GONE
            holder.mint.visibility=View.VISIBLE
            holder.text_min.visibility=View.VISIBLE
            holder.sec.visibility=View.VISIBLE
            holder.text_sec.visibility=View.VISIBLE
        }
        else
        {
            if(AppInfo.hour=="0" && AppInfo.mint=="0" && AppInfo.sec!="0") {
                holder.hour.visibility = View.GONE
                holder.text_hr.visibility = View.GONE
                holder.mint.visibility = View.GONE
                holder.text_min.visibility = View.GONE
                holder.sec.visibility = View.VISIBLE
                holder.text_sec.visibility = View.VISIBLE
            }
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var appName: TextView = itemView.findViewById(R.id.list_app_name)
        var appIcon: ImageView = itemView.findViewById(R.id.list_app_icon)
        var hour: TextView = itemView.findViewById(R.id.hour)
        var mint: TextView = itemView.findViewById(R.id.minute)
        var sec: TextView = itemView.findViewById(R.id.second)
        var text_hr: TextView = itemView.findViewById(R.id.text_hr)
        var text_min: TextView = itemView.findViewById(R.id.text_min)
        var text_sec: TextView = itemView.findViewById(R.id.text_sec)
        var usage_list:ConstraintLayout=itemView.findViewById(R.id.usage_list)

    }
}
