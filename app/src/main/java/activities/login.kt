package activities

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.firebase.auth.FirebaseAuth
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.login.*
//import kotlinx.android.synthetic.main.login.email
//import kotlinx.android.synthetic.main.login.password
import java.util.*

class login : Fragment()
    //,View.OnClickListener
{
    var dialog: AlertDialog? = null

    private var pd: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  init()
    }
//    private fun init() {
//        listeners()
//        dialogSetup()
//    }
//
//    private fun dialogSetup()
//    {
//        pd = ProgressDialog(requireActivity())
//        pd!!.setMessage("Loading...")
//        pd!!.setCancelable(true)
//        pd!!.setCanceledOnTouchOutside(false)
//    }
//
//    private fun listeners() {
//        signIn_button_login.setOnClickListener(this)
//        signup_button.setOnClickListener(this)
//        google_constraint.setOnClickListener(this)
//    }
//    private fun openDialog() {
//        val alert: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Dialog_Alert)
//        } else {
//            AlertDialog.Builder(requireContext())
//        }
//        val inflater = layoutInflater
//        val view = inflater.inflate(R.layout.error_dialog_login, null)
//        val email_error = view.findViewById<TextView>(R.id.error_text_1)
//        val password_error = view.findViewById<TextView>(R.id.error_text_2)
//        val ok = view.findViewById<AppCompatButton>(R.id.ok_button)
//        if( Sharepref.getBoolean(requireContext(),Constants.EMAIL_PASSWORD_EMPTY,false))
//        {
//            email_error.text=getString(R.string.email_error_1)
//            password_error.text=getString(R.string.password_error_1)
//        }
//        else if(Sharepref.getBoolean(requireContext(),Constants.EMAIL_EMPTY,false))
//        {
//            email_error.text=getString(R.string.email_error_1)
//            password_error.visibility=View.GONE
//        }
//        else if(Sharepref.getBoolean(requireContext(),Constants.EMAIL_PATTERN_ERROR,false))
//        {
//            email_error.text=getString(R.string.email_error_2)
//            password_error.visibility=View.GONE
//        }
//        else if(Sharepref.getBoolean(requireContext(),Constants.PASSWORD_EMPTY,false))
//        {
//            email_error.visibility=View.GONE
//            password_error.visibility=View.VISIBLE
//            password_error.text=getString(R.string.password_error_1)
//        }
//        else if(Sharepref.getBoolean(requireContext(),Constants.PASSWORD_LENGTH,false))
//        {
//            email_error.visibility=View.GONE
//            password_error.visibility=View.VISIBLE
//            password_error.text=getString(R.string.password_error_2)
//        }
//
//
//        ok.setOnClickListener {
//            //findNavController().navigate(R.id.action_nav_password_to_nav_dashboard)
//            openLoginPage()
//            dialog!!.cancel()
//
//        }
//        alert.setView(view)
//        alert.setCancelable(true)
//        dialog = alert.create()
//        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog!!.show()
//    }
//    private fun openLoginPage() {
//        val navHostFragment =
//            requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        val navGraph = navController.navInflater.inflate(R.navigation.my_nav)
//        navController.navigate(R.id.loginFragment)
//    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.signIn_button_login -> {
//                if(email.text.toString().isEmpty() && password.text.toString().isEmpty()){
//                    Sharepref.setBoolean(requireContext(),Constants.EMAIL_PASSWORD_EMPTY,true)
//                    openDialog()
//
//                }
//                else if(email.text.toString().isEmpty())
//                {
//                    Sharepref.setBoolean(requireContext(),Constants.EMAIL_EMPTY,true)
//                    openDialog()
//                }
//                else if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
//                {
//                    Sharepref.setBoolean(requireContext(),Constants.EMAIL_PATTERN_ERROR,true)
//                    openDialog()
//                }
//                else if(password.text.toString().isEmpty())
//                {
//                    Sharepref.setBoolean(requireContext(),Constants.PASSWORD_EMPTY,true)
//                    openDialog()
//                }
//                else if(password.text.toString().isEmpty())
//                {
//                    Sharepref.setBoolean(requireContext(),Constants.PASSWORD_EMPTY,true)
//                    openDialog()
//                }
//                else if(password.text.length<8)
//                {
//                    Sharepref.setBoolean(requireContext(),Constants.PASSWORD_LENGTH,true)
//                    openDialog()
//                }
//                else{
//                    if (email.text.isNotEmpty() && password.text.isNotEmpty())
//                    {
//                        Sharepref.setString(requireContext(),Constants.LOGIN_USER_EMAIL,email.text.toString())
//                        Sharepref.setString(requireContext(),Constants.LOGIN_USER_PASSWORD,password.text.toString())
//                        val encodedPassword: String = Base64.getEncoder().encodeToString(password.text.toString().toByteArray())
//
//                        pd!!.show()
//                        mAuth = FirebaseAuth.getInstance()
//                        mAuth!!.signInWithEmailAndPassword(email.text.toString(), encodedPassword)
//                            .addOnCompleteListener(requireActivity()) { task ->
//                                if (!task.isSuccessful) {
//                                    Toast.makeText(
//                                        requireActivity(),
//                                        "Authentication Failed",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                    Log.v("error", task.exception!!.message!!)
//                                } else {
//                                    Sharepref.setBoolean(requireActivity(), Constants.IS_LOGIN, true)
//                                    Toast.makeText(
//                                        requireActivity(),
//                                        "Login Successful!",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    val id = mAuth!!.currentUser!!.uid
//                                    Sharepref.setString(requireActivity(), Constants.CURRENT_USER_UID, id)
//                                    Sharepref.setString(requireActivity(), Constants.USER_EMAIL, email.text.toString())
//                                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//                                }
//                                pd!!.dismiss()
//                            }
//                    }
//                }
//               // findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//            }
//            R.id.signup_button ->
//        {
//
//            }
//            R.id.google_constraint -> {
//
//            }
//        }
//    }
}
