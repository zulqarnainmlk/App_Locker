package fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_reset_pin.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.tvTitle
import listeners.HomeListener
import java.util.*


class UpdateFragment : Fragment(), View.OnClickListener {
    var dialog: AlertDialog? = null
    private var mAuth: FirebaseAuth? = null
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
            newTitle = "Change Password"


        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
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
            tvTitle.text = displayName

        } else {


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
                    val displayName = "$firstName $lastName"
                    tvTitle.text = displayName!!

                    Log.e("Testing: ", firstName!! + "\n" + lastName!!)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })


        }
    }

    private fun openDialog() {
        val alert: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(requireContext())
        }
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.update_password_dialog, null)
        val ok = view.findViewById<AppCompatButton>(R.id.ok_button)

        ok.setOnClickListener {
            //findNavController().navigate(R.id.action_nav_password_to_nav_dashboard)
            findNavController().navigate(R.id.action_updateFragment_to_profileFragment)

            dialog!!.cancel()

        }
        alert.setView(view)
        alert.setCancelable(true)
        dialog = alert.create()
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.show()
    }

    private fun listeners() {
        button_update.setOnClickListener(this)
        visible_pass.setOnClickListener(this)
        invisible_pass.setOnClickListener(this)
        hide_pass.setOnClickListener(this)
        show_pass.setOnClickListener(this)
        hide_pass_1.setOnClickListener(this)
        show_pass_1.setOnClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.visible_pass -> {
                if (visible_pass.isVisible) {
                    visible_pass.visibility = View.GONE
                    current_pass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    invisible_pass.visibility = View.VISIBLE

                }
            }
            R.id.invisible_pass -> {
                if (invisible_pass.isVisible) {
                    invisible_pass.visibility = View.GONE

                    current_pass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    visible_pass.visibility = View.VISIBLE

                }
            }
            R.id.hide_pass -> {

                if (hide_pass.isVisible) {
                    hide_pass.visibility = View.GONE

                    new_pass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    show_pass.visibility = View.VISIBLE

                }
            }
            R.id.show_pass -> {
                if (show_pass.isVisible) {
                    show_pass.visibility = View.GONE
                    new_pass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    hide_pass.visibility = View.VISIBLE


                }
            }
            R.id.hide_pass_1 -> {

                if (hide_pass_1.isVisible) {
                    hide_pass_1.visibility = View.GONE

                    con_pass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    show_pass_1.visibility = View.VISIBLE

                }
            }
            R.id.show_pass_1 -> {
                if (show_pass_1.isVisible) {
                    show_pass_1.visibility = View.GONE
                    con_pass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    hide_pass_1.visibility = View.VISIBLE


                }
            }

            R.id.button_update -> {

                if (current_pass.text.isEmpty() && new_pass.text.isEmpty() && con_pass.text.isEmpty()) {
                    current_pass_error.visibility = View.VISIBLE
                    new_pass_error.visibility = View.VISIBLE
                    con_pass_error.visibility = View.VISIBLE
                    current_pass_error.text = "please fill this field with length of minimum 8"
                    new_pass_error.text = "please fill this field with length of minimum 8"
                    con_pass_error.text = "please fill this field with length of minimum 8"
                } else if (current_pass.text.length < 8) {
                    current_pass_error.visibility = View.VISIBLE
                    new_pass_error.visibility = View.GONE
                    con_pass_error.visibility = View.GONE
                    current_pass_error.text = "please fill this field with length of minimum 8"


                } else if (new_pass.text.isEmpty()) {
                    current_pass_error.visibility = View.GONE
                    new_pass_error.visibility = View.VISIBLE
                    con_pass_error.visibility = View.GONE
                    new_pass_error.text = "please fill this field with length of minimum 8"

                } else if (new_pass.text.length < 8) {
                    current_pass_error.visibility = View.GONE
                    new_pass_error.visibility = View.VISIBLE
                    con_pass_error.visibility = View.GONE
                    new_pass_error.text = "please fill this field with length of minimum 8"
                } else if (con_pass.text.isEmpty()) {
                    current_pass_error.visibility = View.GONE
                    new_pass_error.visibility = View.GONE
                    con_pass_error.visibility = View.VISIBLE
                    con_pass_error.text = "please fill this field with length of minimum 8"

                } else if (con_pass.text.length < 8) {
                    current_pass_error.visibility = View.GONE
                    new_pass_error.visibility = View.GONE
                    con_pass_error.visibility = View.VISIBLE
                    con_pass_error.text = "please fill this field with length of minimum 8"
                } else if (new_pass.text.toString() != con_pass.text.toString()) {
                    current_pass_error.visibility = View.GONE
                    new_pass_error.visibility = View.VISIBLE
                    con_pass_error.visibility = View.VISIBLE
                    new_pass_error.text = "password is not same"
                    con_pass_error.text = "password is not same"

                } else if (current_pass.text.toString() != Sharepref.getString(
                        requireContext(),
                        Constants.REGISTER_USER_PASSWORD,
                        ""
                    ) ||
                    current_pass.text.toString() != Sharepref.getString(
                        requireContext(),
                        Constants.LOGIN_USER_PASSWORD,
                        ""
                    )
                ) {
                    current_pass_error.visibility = View.VISIBLE
                    new_pass_error.visibility = View.GONE
                    con_pass_error.visibility = View.GONE
                    current_pass_error.text = "invalid password"

                } else {
                    current_pass_error.visibility = View.GONE
                    new_pass_error.visibility = View.GONE
                    con_pass_error.visibility = View.GONE
                    val user = FirebaseAuth.getInstance().currentUser
                    val encodedPassword =
                        Base64.getEncoder().encodeToString(new_pass.text.toString().toByteArray())
                    try
                    {
                        user!!.updatePassword(encodedPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.e("updated", "User password updated.")
                                }
                            }
                    }
                    catch(ex:Exception)
                    {

                    }


                    db_User = FirebaseDatabase.getInstance()
                    db_ref = db_User!!.getReference("CustomUsers")

                    mAuth = FirebaseAuth.getInstance()
                    val id = mAuth!!.currentUser!!.uid
                    Log.e("current:", id)
                    db_ref!!.child(id).child("password").setValue(encodedPassword)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                openDialog()

                                // Toast.makeText(requireContext(), "password-upgraded", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "failed to upgrade password",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                }


            }


        }


    }


}