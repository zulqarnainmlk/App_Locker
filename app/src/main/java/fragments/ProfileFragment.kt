package fragments

import android.content.Context
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
import listeners.HomeListener


class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var googleSignInClient: GoogleSignInClient
    var mAuth: FirebaseAuth? = null
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
            toolbarVisibility = true,
            backBtnVisibility = true,
            newTitle = "Profile"


        )
    }

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
    }

    private fun init() {
        listeners()
        checkUser()

    }

    private fun checkUser() {
        if (Sharepref.getBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, false)) {
            val displayName = Sharepref.getString(requireContext(), Constants.DISPLAY_NAME, "")
            user_name.text = displayName
            constraint_logout.visibility = View.GONE
            constraint_changePass.visibility = View.GONE
            constraint_logout_gmail.visibility = View.VISIBLE
        } else {
            constraint_logout.visibility = View.VISIBLE
            constraint_changePass.visibility = View.VISIBLE
            constraint_logout_gmail.visibility = View.GONE

            val uid = Sharepref.getString(requireActivity(), Constants.CURRENT_USER_UID, "")
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
                    try {
                        val displayName = "$firstName $lastName"
                        user_name.text = displayName
                    }
                    catch (ex:Exception)
                    {

                    }


                    Log.e("Testing: ", firstName!! + "\n" + lastName!!)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })


        }
    }


    private fun listeners() {

        constraint_resetPIN.setOnClickListener(this)
        constraint_changePass.setOnClickListener(this)
        constraint_pin_biometric.setOnClickListener(this)
        constraint_logout_gmail.setOnClickListener(this)
        constraint_logout.setOnClickListener(this)
        constraint_logout_gmail.setOnClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.constraint_pin_biometric -> {
                findNavController().navigate(R.id.action_profileFragment_to_biometricFragment)

            }

            R.id.constraint_resetPIN -> {
                findNavController().navigate(R.id.action_profileFragment_to_resetPinFragment)

            }
            R.id.constraint_changePass -> {
                findNavController().navigate(R.id.action_profileFragment_to_updateFragment)
            }

            R.id.constraint_logout_gmail -> {
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

                googleSignInClient.signOut().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.e("signout:", "gmail-signout")
                        Sharepref.setBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, false)

                        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

                    } else {
                        Log.e("signout", "gmail-signout-failed")

                    }
                }



                }
            R.id.constraint_logout -> {

                    FirebaseAuth.getInstance().signOut()
                    Sharepref.setBoolean(requireActivity(), Constants.IS_LOGIN, false)
                    Sharepref.setBoolean(requireActivity(), Constants.IS_SIGNUP, false)
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }


            }
        }
    }


