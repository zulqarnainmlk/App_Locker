package fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.facebook.*
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.logging.Logger


class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var callbackManager: CallbackManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
       // LoginManager.getInstance().logOut()

        onFbClick()
        onGmailClick()
        onCustomCLick()
    }

    private fun onCustomCLick()
    {
        customLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }



    private fun onFbClick()
    {
        fbLogin.setOnClickListener {
            Log.e("onClick:", "pressed")
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("advanced access", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Log.e("onClick:", "Facebook token: " + result.accessToken.token)

                        val graphRequest =
                            GraphRequest.newMeRequest(result.accessToken) {`object`, response ->
                                getFacebookData(`object`)
                                Log.e("onClick:", "Facebook object: $`object`")
                            }
                        val parameters = Bundle()
                        parameters.putString("fields","id,email,birthday,friends,gender,name")
                        graphRequest.parameters = parameters
                        graphRequest.executeAsync()

                    }

                    override fun onCancel() {
                        Log.e("onClick", "Facebook onCancel.")
                        Toast.makeText(requireActivity(), "Login Cancelled", Toast.LENGTH_SHORT)
                            .show()
//                        val token=AccessToken.getCurrentAccessToken()
//                        if(token!=null)
//                        {
//                            val graphRequest =
//                                GraphRequest.newMeRequest(token) {`object`, response ->
//                                    getFacebookData(`object`)
//                                    Log.e("onClick:", "Facebook object: $`object`")
//                                }
//                            val parameters = Bundle()
//                            parameters.putString("fields","id,email,birthday,friends,gender,name")
//                            graphRequest.parameters = parameters
//                            graphRequest.executeAsync()
//                        }

                    }
//                        AccessToken token = AccessToken.getCurrentAccessToken();
//                        if (token != null) {
//                            GraphRequest request = GraphRequest.newMeRequest(
//                                    token,
//                            new GraphRequest.GraphJSONObjectCallback() {
//                                @Override
//                                public void onCompleted(
//                                    JSONObject object,
//                                    GraphResponse response) {
//                                    FBObject = response.getJSONObject();
//
//                                }
//                            });
//                            Bundle parameters = new Bundle();
//                            parameters.putString("fields", "id,name,email,gender, birthday");
//                            request.setParameters(parameters);
//                            request.executeAsync();
//                    }

                    override fun onError(error: FacebookException) {
                        Log.e("onClick", "Facebook onError.")
                        Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }


    }


    private fun getFacebookData(obj: JSONObject?) {
        Log.e("onClick", obj.toString())
         val profilePic = "https://graph.facebook.com/${obj?.getString("id")}/picture?width=200&height=200"
        val name = obj?.getString("name")
        val birthday = obj?.getString("birthday")
        val gender = obj?.getString("gender")
        val email = obj?.getString("email")
        Log.e("FacebookLogin", "name:${name}")


    }
    private fun onGmailClick() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        gmailLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
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
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                val bundle = Bundle()
                bundle.putString("email", account.email)
                bundle.putString("name", account.displayName)
                val fragment = HomeFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.commit()
                Sharepref.setBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, true)
              //  findNavController().navigate(R.id.action_signupFragment_to_homeFragment,bundle)


            } else {
                Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}