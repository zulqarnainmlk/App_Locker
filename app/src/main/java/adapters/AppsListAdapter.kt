package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_locker.R
import models.AppInfo

class AppsListAdapter(private var mList: ArrayList<AppInfo>) : RecyclerView.Adapter<AppsListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.installed_app_list, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val AppInfo = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageDrawable(AppInfo.icon)
        // sets the text to the textview from our itemHolder class
        holder.name.text = AppInfo.appName
        holder.package_name.text = AppInfo.packageName

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }
    fun filterList(filteredList: ArrayList<AppInfo>) {
        mList = filteredList
        notifyDataSetChanged()
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.app_icon)
        val name: TextView = itemView.findViewById(R.id.list_app_name)
        val package_name: TextView = itemView.findViewById(R.id.app_package)
    }
}
