package fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.error_dialog_login.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.email
import kotlinx.android.synthetic.main.fragment_login.google_constraint
import kotlinx.android.synthetic.main.fragment_login.password
import kotlinx.android.synthetic.main.fragment_login.signIn_button_login
import kotlinx.android.synthetic.main.fragment_login.signup_button
import kotlinx.android.synthetic.main.login.*
import listeners.HomeListener
import java.util.*


class LoginFragment : Fragment(), View.OnClickListener{
    var dialog: AlertDialog? = null

    private var pd: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var callbackManager: CallbackManager? = null
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null
    private lateinit var homeListener: HomeListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }
    override fun onResume() {
        super.onResume()
        homeListener.onHomeDataChangeListener(
            toolbarVisibility = false,
            backBtnVisibility = false,
            newTitle = ""


        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        listeners()
        dialogSetup()
    }

    private fun dialogSetup()
    {
        pd = ProgressDialog(requireActivity())
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(true)
        pd!!.setCanceledOnTouchOutside(false)
    }

    private fun openDialog(email_error: String, password_error: String) {
        val alert: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(requireContext())
        }
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.error_dialog_login, null)
        val email_error_text = view.findViewById<TextView>(R.id.error_text_1)
        val password_error_text = view.findViewById<TextView>(R.id.error_text_2)
        val ok = view.findViewById<AppCompatButton>(R.id.ok_button)
        email_error_text.text=email_error
        password_error_text.text=password_error

        ok.setOnClickListener {
            //findNavController().navigate(R.id.action_nav_password_to_nav_dashboard)
            openLoginPage()
            dialog!!.cancel()

        }
        alert.setView(view)
        alert.setCancelable(true)
        dialog = alert.create()
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.show()
    }
    private fun openLoginPage() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.my_nav)
        navController.navigate(R.id.loginFragment)
    }
    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(requireActivity(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

//                val bundle = Bundle()
//                bundle.putString("email", account.email)
//                bundle.putString("name", account.displayName)
//                val fragment = HomeFragment()
//                fragment.arguments = bundle
//                fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.commit()
                Sharepref.setString(requireContext(),Constants.DISPLAY_NAME,account.displayName.toString())
                db_User = FirebaseDatabase.getInstance()
                db_ref = db_User!!.getReference("GmailUsers")
                db_ref!!.child(account.id!!).child("displayName").setValue(account.displayName)
                db_ref!!.child(account.id!!).child("familyName").setValue(account.familyName)
                db_ref!!.child(account.id!!).child("givenName").setValue(account.givenName)
                db_ref!!.child(account.id!!).child("idToken").setValue(account.idToken)
                db_ref!!.child(account.id!!).child("email").setValue(account.email)
                db_ref!!.child(account.id!!).child("photoUrl").setValue(account.photoUrl.toString())
                Log.e("data", account.id!!)
                Log.e("data", account.displayName!!)
                Log.e("data", account.familyName!!)
                Log.e("data", account.givenName!!)
                Log.e("data", account.idToken!!)
                Log.e("data", account.email!!)
                Log.e("data", account.photoUrl!!.toString())
                Sharepref.setString(requireContext(),Constants.CURRENT_GMAIL_USER_UID,account.id.toString())



                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                Sharepref.setBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, true)
            } else {
                Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
    private fun listeners() {
        password_visible.setOnClickListener(this)
        password_invisible.setOnClickListener(this)
        signIn_button_login.setOnClickListener(this)
        signup_button.setOnClickListener(this)
        google_constraint.setOnClickListener(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.password_visible ->
            {
                if(password_visible.isVisible)
                {
                    password_visible.visibility=View.GONE
                    password.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    password_invisible.visibility=View.VISIBLE


                }
            }
            R.id.password_invisible ->
            {
                if(password_invisible.isVisible)
                {
                    password_invisible.visibility=View.GONE

                    password.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    password_visible.visibility=View.VISIBLE

                }
            }
            R.id.signIn_button_login -> {

                if(email.text.toString().isEmpty() && password.text.toString().isEmpty())
                {
                    openDialog("please enter valid email","please enter valid password")
                }
                else if(email.text.toString().isEmpty())
                {
                    openDialog("please enter valid email", "")
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
                {
                    openDialog("please enter valid email", "")
                }
                else if(password.text.toString().isEmpty())
                {
                    openDialog("", "please enter valid password")
                }

                else if(password.text.length<8)
                {
                    openDialog("", "length of password is minimum 8")
                }
                else{
                    if (email.text.isNotEmpty() && password.text.isNotEmpty())
                    { if(isConnected()) {
                        Sharepref.setString(
                            requireContext(),
                            Constants.LOGIN_USER_EMAIL,
                            email.text.toString()
                        )
                        Sharepref.setString(
                            requireContext(),
                            Constants.LOGIN_USER_PASSWORD,
                            password.text.toString()
                        )
                        val encodedPassword: String = Base64.getEncoder()
                            .encodeToString(password.text.toString().toByteArray())

                        pd!!.show()
                        mAuth = FirebaseAuth.getInstance()
                        mAuth!!.signInWithEmailAndPassword(email.text.toString(), encodedPassword)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Authentication Failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.v("error", task.exception!!.message!!)
                                } else {
                                    Sharepref.setBoolean(
                                        requireActivity(),
                                        Constants.IS_LOGIN,
                                        true
                                    )
                                    Toast.makeText(
                                        requireActivity(),
                                        "Login Successful!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val id = mAuth!!.currentUser!!.uid
                                    Sharepref.setString(
                                        requireActivity(),
                                        Constants.CURRENT_USER_UID,
                                        id
                                    )
                                    Sharepref.setString(
                                        requireActivity(),
                                        Constants.USER_EMAIL,
                                        email.text.toString()
                                    )
                                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                }
                                pd!!.dismiss()
                            }
                    }
                    else {
                        Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()

                    }
                    }
                }
                // findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
            R.id.signup_button ->
            {
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
            R.id.google_constraint -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }
    }

//    private fun listeners() {
//        signup.setOnClickListener(this)
//        button_login.setOnClickListener(this)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.signup ->
//            {
//                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
//            }
//            R.id.button_login ->
//            {
//                val email = useremail.text.toString()
//                val password = userpassword.text.toString()
//                try {
//                    if(email.isEmpty() && password.isEmpty()) {
//                        //    Toast.makeText(applicationContext, "Please fill all the field.", Toast.LENGTH_LONG).show()
//                        useremail.error = "enter valid email"
//                        userpassword.error = "enter valid password"
//                    }
//
//                    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//                    {
//                        useremail.error="please re-enter your email"
//                    }
//                    else if(password.isEmpty())
//                    {
//                        userpassword.error = "enter valid password"
//
//                    }
//                    else if(password.length<8)
//                    {
//                        userpassword.error="length of password is minimum 6"
//                    }
//                    else  {
//                        if (password.isNotEmpty() && email.isNotEmpty())
//                        {
//                            Sharepref.setString(requireContext(),Constants.LOGIN_USER_EMAIL,email)
//                            Sharepref.setString(requireContext(),Constants.LOGIN_USER_PASSWORD,password)
//                            val encodedPassword: String = Base64.getEncoder().encodeToString(password.toByteArray())
//
//                            pd!!.show()
//                            mAuth = FirebaseAuth.getInstance()
//                            mAuth!!.signInWithEmailAndPassword(email, encodedPassword)
//                                .addOnCompleteListener(requireActivity()) { task ->
//                                    if (!task.isSuccessful) {
//                                        Toast.makeText(
//                                            requireActivity(),
//                                            "Authentication Failed",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                        Log.v("error", task.exception!!.message!!)
//                                    } else {
//                                        Sharepref.setBoolean(requireActivity(), Constants.IS_LOGIN, true)
//                                        Toast.makeText(
//                                            requireActivity(),
//                                            "Login Successful!",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        val id = mAuth!!.currentUser!!.uid
//                                        Sharepref.setString(requireActivity(), Constants.CURRENT_USER_UID, id)
//                                        Sharepref.setString(requireActivity(), Constants.USER_EMAIL, email)
//                                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//                                    }
//                                    pd!!.dismiss()
//                                }
//                        }
//
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//    }
//
//    private fun signUpClick()
//    {
//        signup.setOnClickListener{
//            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
//        }
//    }
//    private fun loginClick()
//    {
//        button_login.setOnClickListener {
//            val email = useremail.text.toString()
//            val password = userpassword.text.toString()
//            try {
//                if(email.isEmpty() && password.isEmpty()) {
//                    //    Toast.makeText(applicationContext, "Please fill all the field.", Toast.LENGTH_LONG).show()
//                    useremail.error = "enter valid email"
//                    userpassword.error = "enter valid password"
//                }
//
//                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//                {
//                    useremail.error="please re-enter your email"
//                }
//                else if(password.isEmpty())
//                {
//                    userpassword.error = "enter valid password"
//
//                }
//                else if(password.length<8)
//                {
//                    userpassword.error="length of password is minimum 6"
//                }
//                else  {
//                    if (password.isNotEmpty() && email.isNotEmpty())
//                    {
//                        pd!!.show()
//                        mAuth!!.signInWithEmailAndPassword(email, password)
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
//                                    Sharepref.setString(requireActivity(), Constants.USER_EMAIL, email)
//                                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//
//
//
//                                }
//                                pd!!.dismiss()
//                            }
//                    }
//
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//
//
//
//
//    }
//
//


}