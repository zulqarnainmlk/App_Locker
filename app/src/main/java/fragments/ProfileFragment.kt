package fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var googleSignInClient: GoogleSignInClient
    var mAuth: FirebaseAuth? = null
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        title.text = getString(R.string.profile)
    }

    private fun init() {
        listeners()
        checkUser()

    }

    private fun checkUser() {
        if (Sharepref.getBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, false))
        {
            val displayName=Sharepref.getString(requireContext(),Constants.DISPLAY_NAME,"")
            user_name.text=displayName
            change_pass_card.visibility=View.GONE
        }
        else
        {    val uid= Sharepref.getString(requireActivity(), Constants.CURRENT_USER_UID, "")
            db_User = FirebaseDatabase.getInstance()
            db_ref = db_User!!.getReference("CustomUsers").child(uid!!)
            db_ref!!.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val firstName = dataSnapshot.child("firstname").getValue(
                        String::class.java
                    )
                    val lastName = dataSnapshot.child("lastname").getValue(
                        String::class.java
                    )
                    val displayName= "$firstName $lastName"
                    user_name.text = displayName!!

                    Log.e("Testing: ", firstName!! + "\n" +lastName!!)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })




        }
    }


    private fun listeners() {
        view_back.setOnClickListener(this)
        home_tab.setOnClickListener(this)
        apps_tab.setOnClickListener(this)
        vault_tab.setOnClickListener(this)
        profile_tab.setOnClickListener(this)
        reset_card.setOnClickListener(this)
        change_pass_card.setOnClickListener(this)
        set_Pin.setOnClickListener(this)
        button_logout.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.view_back -> {
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            }
            R.id.set_Pin->{
                findNavController().navigate(R.id.action_profileFragment_to_biometricFragment)

            }

            R.id.reset_card -> {
                findNavController().navigate(R.id.action_profileFragment_to_resetPinFragment)

            }
            R.id.change_pass_card -> {
                findNavController().navigate(R.id.action_profileFragment_to_updateFragment)
            }

            R.id.button_logout -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
//                googleSignInClient.signOut().addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        Log.e("signout", "gmail-signout")
//                    } else {
//                        Log.e("signout", "gmail-signout-failed")
//
//                    }
//                }
                if (Sharepref.getBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, false)

                ) {
                    googleSignInClient.signOut().addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.e("signout:", "gmail-signout")
                            Sharepref.setBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, false)

                            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

                        } else {
                            Log.e("signout", "gmail-signout-failed")

                        }
                    }

                } else {
                    FirebaseAuth.getInstance().signOut()
                    Sharepref.setBoolean(requireActivity(), Constants.IS_LOGIN, false)
                    Sharepref.setBoolean(requireActivity(), Constants.IS_SIGNUP, false)
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
            R.id.home_tab -> {
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)

            }
            R.id.apps_tab -> {
                findNavController().navigate(R.id.action_profileFragment_to_appsFragment)
            }
            R.id.vault_tab -> {
                findNavController().navigate(R.id.action_profileFragment_to_vaultFragment)

            }
            R.id.profile_tab -> {
                Toast.makeText(requireContext(), "Profile", Toast.LENGTH_SHORT).show()

            }

        }
    }

}