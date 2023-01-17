package services

import activities.BiometricActivity
import activities.LockActivity
import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.app.KeyguardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.*
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.app_locker.R
import database.VaultDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class MyAccessibilityService : AccessibilityService() {

    private var currentActivity: String = ""
    private var packagName = ""
    private var context: Context? = null
    private var activity:Activity?=null
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val vaultDatabase by lazy { VaultDatabase.getDatabase(this).vaultDao() }



    private fun tryGetActivity(componentName: ComponentName): ActivityInfo? {
        try {
            return packageManager.getActivityInfo(componentName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
         Log.e("tagaccess", "onAccessibilityEvent:  " + event)
        var recentApp = ""
        when (event.eventType  ) {
            TYPE_WINDOW_STATE_CHANGED -> {


                val pkgname :String = event.packageName.toString()
                scope.launch {
                    vaultDatabase.getData().collect { data ->


                        val result = data.filter { it.pkgInfo == pkgname }


                        if (result.isNotEmpty()) {
                            Log.e("new", "App blocked " + result.size)

                            if(result[0].status==true){

                                if (Sharepref.getBoolean(context!!,"recent_app_status",true)){
                                    if( Sharepref.getString(context!!, Constants.PIN_GENERATED,"")!!.isNotEmpty()) {
                                        val intent =
                                            Intent(applicationContext, LockActivity::class.java)
                                        intent.putExtra("pkgname", pkgname)

                                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                    }
                                    else if(Sharepref.getString(context!!,Constants.SET_BIOMETRIC,"")!!.isNotEmpty())
                                    {
//                                        authenticateWithBiometrics()
                                        val intent =
                                            Intent(applicationContext, BiometricActivity::class.java)
                                        intent.putExtra("pkgname", pkgname)

                                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)

                                    }
                                }


                            }

                        } else {



                            if(Sharepref.getString(context!!,"recent_package","com.sec.android.app.launcher")!=pkgname && pkgname!="com.sec.android.app.launcher" ){
                                Log.e("dbjo", "timeline :$pkgname")
                                Sharepref.setString(context!!,"recent_package",pkgname.toString())

                                if (Sharepref.getBoolean(context!!,"app_locked",false)){

                                    vaultDatabase.getAllApps().collect { data ->
//
                                        val result1 = data.filter { it.pkgInfo == pkgname }
//
                                        Log.e("dbjo", "App blocked else ")
                                        //if( "com.shazam.android"== pkgname){
                                        if(result1.isNotEmpty())
                                        {
                                            Log.e("dbjo", "DB status Change")
                                            Sharepref.setBoolean(context!!, "app_locked", false)
                                            Sharepref.setBoolean(context!!, "recent_app_status", true)
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }


//            TYPE_WINDOW_CONTENT_CHANGED->{ Log.e("tag1122", "TYPE_WINDOW_CONTENT_CHANGED ")
//                Sharepref.setBoolean(context!!, "recent_app_status", true)
//
//            }
        }
    }



    override fun onInterrupt() {
        Log.e("tag1122", "onInterrupt:something went wrong  ")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        context = applicationContext

        Log.e("tag1122", "onServiceConnected:  ")
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {



        Log.e("tag871122", "event!!.keyCode:  "+event!!.keyCode)


        if (event!!.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_HOME) {
            Toast.makeText(context,"HOme is pressed",Toast.LENGTH_SHORT).show()
            return true
        }
 return false
    }



}
