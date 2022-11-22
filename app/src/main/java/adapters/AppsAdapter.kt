package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_locker.R
import kotlinx.android.synthetic.main.vault_list.view.*
import listeners.AdapterListener
import models.AppList


class AppsAdapter(
    private var context: Context,
    var adapterListener: AdapterListener,
    private var mList: ArrayList<AppList>
) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {
    var isFirstTime = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vault_list, parent, false)

        return AppsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppsAdapter.ViewHolder, position: Int) {


        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageDrawable(mList[position].icon) // sets the text to the textview from our itemHolder class
        holder.name.text = mList[position].appName
        holder.package_name.text = mList[position].packageName


        holder.switch.isChecked = mList[position].status






        holder.itemView.toggle_click.setOnClickListener {
            if (holder.switch.isChecked) {
                holder.switch.isChecked = false
                adapterListener.adapterData("unblock", position)
            } else {
                holder.switch.isChecked = true
                adapterListener.adapterData("block", position)
            }
        }


//        holder.switch.setOnCheckedChangeListener { Button, ischecked ->
//
//
//            if (ischecked) {
//
//                adapterListener.adapterData("block", position)
//            } else {
//                adapterListener.adapterData("unblock", position)
//            }
//
//        }


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return mList.size

    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.app_name)
        val package_name: TextView = itemView.findViewById(R.id.package_name)
        var switch: SwitchCompat = itemView.findViewById(R.id.switchButton)


    }
}