package fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_registration.*
import java.lang.Exception
import java.util.*


class RegistrationFragment : Fragment(), View.OnClickListener {

    var SELECT_IMAGE_CODE = 1
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null
    var uri: Uri? = null
    private var mProfileUri: Uri? = null
    var mAuth: FirebaseAuth? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        listeners()
    }

    private fun listeners() {
        Cam_Icon.setOnClickListener(this)
        button_register.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.Cam_Icon -> {
                Log.e("onClick", "pressed")
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE)
            }
            R.id.button_register -> {
                val First_Name = first_name.text.toString()
                val Last_Name = last_name.text.toString()
                val Email = email.text.toString()
                val Password = password.text.toString()
                val Confirm_Password = confirm_password.text.toString()
                if (First_Name.isEmpty() && Last_Name.isEmpty() && Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty()) {
                    first_name.error =
                        "please fill this field with valid first name having length of greater than 2"
                    last_name.error =
                        "please fill this field with valid last name having length of greater than 2"
                    email.error = "please fill this field with valid email"
                    password.error = "please fill this field with length of minimum 8"
                    confirm_password.error = "please fill this field with length of minimum 8"
                } else if (First_Name.length <= 2) {
                    first_name.error = "FirstName must be equal or greater than 3 characters"
                } else if (Last_Name.isEmpty() && Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty()) {
                    last_name.error =
                        "please fill this field with valid last name having length of greater than 2"
                    email.error = "please fill this field with valid email"
                    password.error = "please fill this field with length of minimum 8"
                    confirm_password.error = "please fill this field with length of minimum 8"
                } else if (Last_Name.length <= 2) {
                    last_name.error = "LastName must be equal or greater than 3 characters"
                } else if (Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty()) {
                    email.error = "please fill this field with valid email"
                    password.error = "please fill this field with length of minimum 8"
                    confirm_password.error = "please fill this field with length of minimum 8"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.error = "invalid email"
                } else if (Password.isEmpty() && Confirm_Password.isEmpty()) {
                    password.error = "please fill this field with length of minimum 8"
                    confirm_password.error = "please fill this field with length of minimum 8"
                } else if (Password.length < 8) {
                    password.error = "please fill this field with length of minimum 8"
                } else if (Confirm_Password.isEmpty()) {
                    confirm_password.error = "please fill this field with length of minimum 8"
                } else if (Password != Confirm_Password) {
                    confirm_password.error = "please enter the same password as above"
                } else {
                    Sharepref.setString(requireContext(),Constants.REGISTER_USER_EMAIL,Email)
                    Sharepref.setString(requireContext(),Constants.REGISTER_USER_PASSWORD,Password)
                    val encodedPassword = Base64.getEncoder().encodeToString(Password.toByteArray())
                    mAuth = FirebaseAuth.getInstance()
                    mAuth!!.createUserWithEmailAndPassword(Email, encodedPassword)
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
                                Sharepref.setString(requireContext(),Constants.CURRENT_Register_USER_UID,id.toString())
                                db_ref!!.child(id).child("firstname").setValue(First_Name)
                                db_ref!!.child(id).child("lastname").setValue(Last_Name)
                                db_ref!!.child(id).child("email").setValue(Email)
                                db_ref!!.child(id).child("password").setValue(encodedPassword)
                                Log.e("tag6116",encodedPassword)
                                Sharepref.setBoolean(requireActivity(), Constants.IS_REGISTER, true)
                                Toast.makeText(
                                    requireContext(),
                                    "your account has been created!",
                                    Toast.LENGTH_LONG
                                ).show()

                                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)

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
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            uri = data?.data
            Log.e("onClick", uri.toString())
            this.uploadImage(uri!!)
            mProfileUri = uri
            profileImg.setImageURI(uri)
        }
    }

    private fun uploadImage(path: Uri) {
        val ref = FirebaseStorage.getInstance().getReference("UserImage/")
        val uploadTask = ref.putFile(path)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Sharepref.setString(
                    requireContext(),
                    Constants.CURRENT_USER_URI,
                    downloadUri.toString()
                )
            } else {
                // Handle failures
                // ...
            }
        }
    }


}






