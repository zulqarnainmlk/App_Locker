package fragments

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.firebase.auth.FirebaseAuth
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception
import java.util.*


class LoginFragment : Fragment(), View.OnClickListener{
    private var pd: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null

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

    private fun listeners() {
        signup.setOnClickListener(this)
        button_login.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.signup ->
            {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            R.id.button_login ->
            {
                val email = useremail.text.toString()
                val password = userpassword.text.toString()
                try {
                    if(email.isEmpty() && password.isEmpty()) {
                        //    Toast.makeText(applicationContext, "Please fill all the field.", Toast.LENGTH_LONG).show()
                        useremail.error = "enter valid email"
                        userpassword.error = "enter valid password"
                    }

                    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        useremail.error="please re-enter your email"
                    }
                    else if(password.isEmpty())
                    {
                        userpassword.error = "enter valid password"

                    }
                    else if(password.length<8)
                    {
                        userpassword.error="length of password is minimum 6"
                    }
                    else  {
                        if (password.isNotEmpty() && email.isNotEmpty())
                        {
                            Sharepref.setString(requireContext(),Constants.LOGIN_USER_EMAIL,email)
                            Sharepref.setString(requireContext(),Constants.LOGIN_USER_PASSWORD,password)
                            val encodedPassword: String = Base64.getEncoder().encodeToString(password.toByteArray())

                            pd!!.show()
                            mAuth = FirebaseAuth.getInstance()
                            mAuth!!.signInWithEmailAndPassword(email, encodedPassword)
                                .addOnCompleteListener(requireActivity()) { task ->
                                    if (!task.isSuccessful) {
                                        Toast.makeText(
                                            requireActivity(),
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.v("error", task.exception!!.message!!)
                                    } else {
                                        Sharepref.setBoolean(requireActivity(), Constants.IS_LOGIN, true)
                                        Toast.makeText(
                                            requireActivity(),
                                            "Login Successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val id = mAuth!!.currentUser!!.uid
                                        Sharepref.setString(requireActivity(), Constants.CURRENT_USER_UID, id)
                                        Sharepref.setString(requireActivity(), Constants.USER_EMAIL, email)
                                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                    }
                                    pd!!.dismiss()
                                }
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun signUpClick()
    {
        signup.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }
    private fun loginClick()
    {
        button_login.setOnClickListener {
            val email = useremail.text.toString()
            val password = userpassword.text.toString()
            try {
                if(email.isEmpty() && password.isEmpty()) {
                    //    Toast.makeText(applicationContext, "Please fill all the field.", Toast.LENGTH_LONG).show()
                    useremail.error = "enter valid email"
                    userpassword.error = "enter valid password"
                }

                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    useremail.error="please re-enter your email"
                }
                else if(password.isEmpty())
                {
                    userpassword.error = "enter valid password"

                }
                else if(password.length<8)
                {
                    userpassword.error="length of password is minimum 6"
                }
                else  {
                    if (password.isNotEmpty() && email.isNotEmpty())
                    {
                        pd!!.show()
                        mAuth!!.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Authentication Failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.v("error", task.exception!!.message!!)
                                } else {
                                    Sharepref.setBoolean(requireActivity(), Constants.IS_LOGIN, true)
                                    Toast.makeText(
                                        requireActivity(),
                                        "Login Successful!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val id = mAuth!!.currentUser!!.uid
                                    Sharepref.setString(requireActivity(), Constants.CURRENT_USER_UID, id)
                                    Sharepref.setString(requireActivity(), Constants.USER_EMAIL, email)
                                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)



                                }
                                pd!!.dismiss()
                            }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }




    }




}