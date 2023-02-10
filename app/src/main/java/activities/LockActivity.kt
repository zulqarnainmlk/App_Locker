package activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import com.example.app_locker.R
import com.goodiebag.pinview.Pinview
import database.VaultDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.activity_lock.*
import kotlinx.coroutines.launch

class LockActivity : AppCompatActivity() {
    private val vaultDatabase by lazy { VaultDatabase.getDatabase(this).vaultDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        val packagename = intent.getStringExtra("pkgname")
        Log.e("new1122", packagename!!)
        lock_pin.setPinViewEventListener(object : Pinview.PinViewEventListener {
                override fun onDataEntered(pinview: Pinview?, fromUser: Boolean) {
                    if (lock_pin.value ==Sharepref.getString(this@LockActivity, Constants.PIN_GENERATED,"")
                    ) {
                        Sharepref.setBoolean(this@LockActivity, "app_locked", true)
                        Sharepref.setBoolean(this@LockActivity, "recent_app_status", false)
                        ActivityCompat.finishAffinity(this@LockActivity)
                        error_box.visibility= View.GONE

                    }
                    else
                    {
                        error_box.visibility= View.VISIBLE
                    }
                }
            })

//        unlock.setOnClickListener {
//            if (lock_pin.value ==Sharepref.getString(this, Constants.PIN_GENERATED,"")
//            ) {
//
//
//                Sharepref.setBoolean(this@LockActivity, "app_locked", true)
//                Sharepref.setBoolean(this@LockActivity, "recent_app_status", false)
//
////                lifecycleScope.launch {
//////Sharepref.setString(this@MainActivity2,"recent_package",packagename)
//////                    vaultDatabase.updateAppStatus(packagename, false)
//////                    Log.e("new1122","app is unblocked")
////
////                }
//                ActivityCompat.finishAffinity(this)
//
//            }
//            else
//            {
//                Toast.makeText(this,"Invalid PIN",Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}
