package fragments

import android.app.Activity
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
import com.example.app_locker.R.*
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.google_constraint
import kotlinx.android.synthetic.main.fragment_signup.password_invisible
import kotlinx.android.synthetic.main.fragment_signup.password_visible
import kotlinx.android.synthetic.main.fragment_signup.signup_button
import org.json.JSONObject
import java.lang.Exception
import java.util.*


class SignupFragment : Fragment(), View.OnClickListener {
    var dialog: AlertDialog? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var callbackManager: CallbackManager? = null
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null
    var mAuth: FirebaseAuth? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        listeners()
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


    private fun openDialog(error_1: String, error_2: String, error_3: String, error_4: String, error_5: String) {
        val alert: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(requireContext())
        }
        val inflater = layoutInflater
        val view = inflater.inflate(layout.error_dialog_signup, null)
        val first_name_error_text = view.findViewById<TextView>(R.id.error_signup_text_1)
        val last_name_error_text = view.findViewById<TextView>(R.id.error_signup_text_2)
        val email_signup_error_text = view.findViewById<TextView>(R.id.error_signup_text_3)
        val password_signup_error_text = view.findViewById<TextView>(R.id.error_signup_text_4)
        val confirm_password_signup_error_text = view.findViewById<TextView>(R.id.error_signup_text_5)
        val ok = view.findViewById<AppCompatButton>(R.id.ok_button)
         first_name_error_text.text=error_1
        last_name_error_text.text=error_2
        email_signup_error_text.text=error_3
        password_signup_error_text.text=error_4
        confirm_password_signup_error_text.text=error_5
        ok.setOnClickListener {
            openSignupPage()
            dialog!!.cancel()

        }
        alert.setView(view)
        alert.setCancelable(true)
        dialog = alert.create()
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.show()
    }
    private fun openSignupPage() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(navigation.my_nav)
        navController.navigate(R.id.signupFragment)
    }
    private fun listeners() {
        signup_button.setOnClickListener(this)
        google_constraint.setOnClickListener(this)
        signin_button.setOnClickListener(this)
        password_visible.setOnClickListener(this)
        password_invisible.setOnClickListener(this)
        cn_password_visible.setOnClickListener(this)
        cn_password_invisible.setOnClickListener(this)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.password_visible ->
            {
                if(password_visible.isVisible)
                {
                    password_visible.visibility=View.GONE
                    password_invisible.visibility=View.VISIBLE
                    password_signup.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }
            }
            R.id.password_invisible ->
            {
                if(password_invisible.isVisible)
                {
                    password_invisible.visibility=View.GONE
                    password_visible.visibility=View.VISIBLE
                    password_signup.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }
            }
            R.id.cn_password_visible ->
            {
                cn_password_signup
                if(cn_password_visible.isVisible)
                {
                    cn_password_visible.visibility=View.GONE
                    cn_password_invisible.visibility=View.VISIBLE
                    cn_password_signup.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }
            }
            R.id.cn_password_invisible ->
            {
                if(cn_password_invisible.isVisible)
                {
                    cn_password_invisible.visibility=View.GONE
                    cn_password_visible.visibility=View.VISIBLE
                    cn_password_signup.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }
            }
            R.id.signup_button -> {

                if (f_name.text.isEmpty() && l_name.text.isEmpty() && email_signup.text.isEmpty() && password_signup.text.isEmpty() && cn_password_signup.text.isEmpty()) {

                    openDialog("please fill this field with valid first name having length of greater than 2",
                        "please fill this field with valid last name having length of greater than 2",
                        "please fill this field with valid email",
                        "please fill this field with length of minimum 8",
                        "please fill this field with length of minimum 8")
                }
                else if (f_name.text.length <= 2)
                {
                    openDialog("please fill this field with valid first name having length of greater than 2",
                        "",
                        "",
                        "",
                        "")
                }
                else if (l_name.text.isEmpty() && email_signup.text.isEmpty() && password_signup.text.isEmpty() && cn_password_signup.text.isEmpty())
                {

                    openDialog("",
                        "please fill this field with valid last name having length of greater than 2",
                        "please fill this field with valid email",
                        "please fill this field with length of minimum 8",
                        "please fill this field with length of minimum 8")
                }
                else if (l_name.text.length <= 2)
                {
                    openDialog("",
                        "please fill this field with valid last name having length of greater than 2",
                        "",
                        "",
                        "")
                }
                else if (email_signup.text.isEmpty() && password_signup.text.isEmpty() && cn_password_signup.text.isEmpty())
                {

                    openDialog("",
                        "",
                        "please fill this field with valid email",
                        "please fill this field with length of minimum 8",
                        "please fill this field with length of minimum 8")
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email_signup.text.toString()).matches())
                {
                    openDialog("",
                        "",
                        "invalid email",
                        "",
                        "")
                }
                else if (password_signup.text.isEmpty() && cn_password_signup.text.isEmpty())
                {

                    openDialog("",
                        "",
                        "",
                        "please fill this field with length of minimum 8",
                        "please fill this field with length of minimum 8")
                }
                else if (password_signup.text.length < 8)
                {
                    openDialog("",
                        "",
                        "",
                        "please fill this field with length of minimum 8",
                        "")
                }
                else if (cn_password_signup.text.isEmpty())
                {
                    openDialog("",
                        "",
                        "",
                        "",
                        "please fill this field with length of minimum 8")
                }
                else if (!password_signup.text.toString().equals(cn_password_signup.text.toString()))
                {
                    openDialog("",
                        "",
                        "",
                        "please enter same password ",
                        "please enter same password ")
                }
                else {
                    if(isConnected()) {
                        Sharepref.setString(
                            requireContext(),
                            Constants.REGISTER_USER_EMAIL,
                            email_signup.text.toString()
                        )
                        Sharepref.setString(
                            requireContext(),
                            Constants.REGISTER_USER_PASSWORD,
                            password_signup.text.toString()
                        )
                        val encodedPassword = Base64.getEncoder()
                            .encodeToString(password_signup.text.toString().toByteArray())
                        mAuth = FirebaseAuth.getInstance()
                        mAuth!!.createUserWithEmailAndPassword(
                            email_signup.text.toString(),
                            encodedPassword
                        )
                            .addOnCompleteListener() { task: Task<AuthResult?> ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Registration failed! try agian.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    Log.v("error", task.exception!!.message!!)

                                } else {

                                    db_User = FirebaseDatabase.getInstance()
                                    db_ref = db_User!!.getReference("CustomUsers")
                                    val id = mAuth!!.currentUser!!.uid
                                    Sharepref.setString(
                                        requireContext(),
                                        Constants.CURRENT_Register_USER_UID,
                                        id.toString()
                                    )
                                    db_ref!!.child(id).child("firstname")
                                        .setValue(f_name.text.toString())
                                    db_ref!!.child(id).child("lastname")
                                        .setValue(l_name.text.toString())
                                    db_ref!!.child(id).child("email")
                                        .setValue(email_signup.text.toString())
                                    db_ref!!.child(id).child("password").setValue(encodedPassword)
                                    Log.e("tag6116", encodedPassword)
                                    Sharepref.setBoolean(
                                        requireActivity(),
                                        Constants.IS_SIGNUP,
                                        true
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "your account has been created!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)

                                }
                            }
                            .addOnFailureListener { e: Exception ->
                                Toast.makeText(
                                    requireContext(),
                                    "invalid login credentials",
                                    Toast.LENGTH_SHORT
                                ).show()


                            }
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    //
                }
                //
            }

            R.id.google_constraint -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
            R.id.signin_button -> {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }
    }

    private fun getFacebookData(obj: JSONObject?) {
        Log.e("onClick", obj.toString())
        val profilePic =
            "https://graph.facebook.com/${obj?.getString("id")}/picture?width=200&height=200"
        val name = obj?.getString("name")
        val birthday = obj?.getString("birthday")
        val gender = obj?.getString("gender")
        val email = obj?.getString("email")
        Log.e("FacebookLogin", "name:${name}")


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



                findNavController().navigate(R.id.action_signupFragment_to_homeFragment)

                Sharepref.setBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, true)
            } else {
                Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}