package activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.app_locker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import listeners.HomeListener
import services.OwnService
import services.Restarter


class MainActivity : AppCompatActivity(), HomeListener, View.OnClickListener {
    var mServiceIntent: Intent? = null
    private var mYourService: OwnService? = null
    private lateinit var navController: NavController




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listeners()
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
//        mYourService = OwnService()
//        mServiceIntent = Intent(this, mYourService!!.javaClass)
//        if (!isMyServiceRunning(mYourService!!.javaClass)) {
//            startService(mServiceIntent)
//        }


    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }




    override fun onHomeDataChangeListener(
        toolbarVisibility: Boolean,
        backBtnVisibility: Boolean,
        newTitle: String
    ) {
        title_toolbar.text = newTitle
        if(toolbarVisibility)
        {
            toolbar.visibility=View.VISIBLE
            if(backBtnVisibility)
            {
                back_button.visibility= View.VISIBLE
            }
            else
            {
                back_button.visibility= View.GONE
            }
        }

        else
        {
            toolbar.visibility=View.GONE
        }
    }
    private fun listeners() {
        back_button.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
//                openHomeFragment()
                this.onBackPressed()
                Log.e("OnClick", "back_button ")
//                val intent = Intent(this, MainActivity2::class.java)
//                startActivity(intent)

            }
        }
    }



//    private fun openHomeFragment() {
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        val navGraph = navController.navInflater.inflate(R.navigation.my_nav)
//        navController.navigate(R.id.homeFragment)    }


}









//tag:LoginBy