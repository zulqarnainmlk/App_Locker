package appusage


import android.app.Activity
import android.content.Context
import android.content.Intent

import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult

import fragments.HomeFragment
import listeners.DialogListener

object Dialog
{

    fun openUsageDialog(context:Context,dialogListener: DialogListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Usage Access Needed :(")
            .setMessage(
                "You need to give usage access to this app to see usage data of your apps. " +
                        "Click \"Go To Settings\" and then give the access :)")
            .setPositiveButton("Go To Settings") { dialog, which ->

                dialogListener.dialogListerData("permission", context as Activity)

            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            .setCancelable(false)
        builder.show()

    }
    fun openAccessibilityDialog(context:Context,dialogListener: DialogListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Accessibility Access Needed :(")
            .setMessage(
                "You need to give accessibility access to this app . " +
                        "Click \"Go To Settings\" and then give the access :)")
            .setPositiveButton("Go To Settings") { dialog, which ->

                dialogListener.dialogListerData("permission1",context as Activity)

            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            .setCancelable(false)
        builder.show()

    }


}