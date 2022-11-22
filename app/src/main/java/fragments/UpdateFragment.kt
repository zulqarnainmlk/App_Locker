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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_update.*
import java.util.*


class UpdateFragment : Fragment(),View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment







        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text = getString(R.string.update_pass)
        init()
    }
    private fun init() {
        listeners()

    }

    private fun listeners() {
        view_back.setOnClickListener(this)
        button_update.setOnClickListener(this)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.view_back -> {
                findNavController().navigate(R.id.action_updateFragment_to_profileFragment)
            }
            R.id.button_update -> {
                val Current_Password = current_pass.text.toString()
                val Update_Password = update_password.text.toString()
                if (Current_Password.isEmpty() && Update_Password.isEmpty()) {
                    current_pass.error = "please fill this field with length of minimum 8"
                } else if (Current_Password.length < 8 && Update_Password.isEmpty()) {
                    current_pass.error = "please fill this field with length of minimum 8"
                    update_password.error = "please fill this field with length of minimum 8"

                } else if (Update_Password.length < 8) {
                    update_password.error = "please fill this field with length of minimum 8"
                } else if (Current_Password != Sharepref.getString(
                        requireContext(),
                        Constants.REGISTER_USER_PASSWORD,
                        ""
                    ) ||
                    Current_Password != Sharepref.getString(
                        requireContext(),
                        Constants.LOGIN_USER_PASSWORD,
                        ""
                    )
                ) {
                    current_pass.error = "invalid password"

                } else {
                    val user = FirebaseAuth.getInstance().currentUser
                    val encodedPassword =
                        Base64.getEncoder().encodeToString(Update_Password.toByteArray())
                    user!!.updatePassword(encodedPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.e("updated", "User password updated.")
                            }
                        }

                    db_User = FirebaseDatabase.getInstance()
                    db_ref = db_User!!.getReference("CustomUsers")

                    mAuth = FirebaseAuth.getInstance()
                    val id = mAuth!!.currentUser!!.uid
                    Log.e("current:", id)
                    db_ref!!.child(id).child("password").setValue(encodedPassword).addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            Toast.makeText(requireContext(), "password-upgraded", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_updateFragment_to_profileFragment)

                        }
                        else
                        {
                            Toast.makeText(requireContext(), "failed to upgrade password", Toast.LENGTH_SHORT).show()

                        }
                    }
                }


            }


        }


    }


}