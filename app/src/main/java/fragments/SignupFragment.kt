package fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.facebook.*
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
import kotlinx.android.synthetic.main.fragment_signup.*
import org.json.JSONObject


class SignupFragment : Fragment(), View.OnClickListener {
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

        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        listeners()
    }

    private fun listeners() {
        gmailLogin.setOnClickListener(this)
        customLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.gmailLogin -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
            R.id.customLogin -> {
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