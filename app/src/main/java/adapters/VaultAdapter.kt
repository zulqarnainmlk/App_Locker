package adapters

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_locker.R
import kotlinx.android.synthetic.main.vault_list.view.*
import listeners.AdapterListener
import models.DbVault
import java.security.AccessController.getContext


class VaultAdapter(
    private var context: Context,
    var adapterListener: AdapterListener,
    private var vault_list: ArrayList<DbVault>
) : RecyclerView.Adapter<VaultAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vault_list, parent, false)

        return VaultAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val AppInfo = vault_list[position]

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageDrawable(AppInfo.icon) // sets the text to the textview from our itemHolder class
        try {
            val icon: Drawable =
                context.packageManager.getApplicationIcon(AppInfo.pkgInfo!!)
            holder.imageView.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        holder.name.text = AppInfo.name
        holder.package_name.text = AppInfo.pkgInfo
        holder.switch.isChecked= true
        holder.switch.setThumbResource(R.drawable.thumb)


        holder.itemView.toggle_click.setOnClickListener {

            if( holder.switch.isChecked){
                adapterListener.adapterVault("unblock", position)
                holder.switch.setThumbResource(R.drawable.thumb1)
            }

        }


//        holder.switch.setOnCheckedChangeListener { Button, ischecked ->
//
//            Log.e("tag123", "status $ischecked")
//
//
//
//        }


    }

    override fun getItemCount(): Int {
        return vault_list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.app_name)
        val package_name: TextView = itemView.findViewById(R.id.package_name)
        var switch: SwitchCompat = itemView.findViewById(R.id.switchButton)
    }
}