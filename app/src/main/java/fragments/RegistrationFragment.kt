package fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
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
import java.util.concurrent.Executor


class RegistrationFragment : Fragment() {
    lateinit var imageView: CircleImageView
    var imagepath: String? = null
    var SELECT_IMAGE_CODE = 1
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null
    var firebaseStorage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var uri: Uri? = null
    private var mProfileUri: Uri? = null
    var mAuth: FirebaseAuth? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        onImageClick()
        onRegistrationClick()
       // imageView = view.findViewById(R.id.profile_image)
    }

    private fun onImageClick() {
        profileCam_button_card.setOnClickListener {
            Log.e("onClick","pressed")
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            uri = data?.data
            Log.e("onClick",uri.toString())
            this.uploadimage(uri!!)

///           Sharepref.setString( requireActivity(), Constants.CURRENT_USER_URI, uri.toString())

            mProfileUri = uri
            profileImg.setImageURI(uri)
        }
    }

    private fun onRegistrationClick() {
        button_register.setOnClickListener {
//            val mountainsRef: StorageReference =
//                storageReference!!.child("images/" + UUID.randomUUID().toString())
//            mountainsRef.putFile(uri!!).addOnSuccessListener(OnSuccessListener<Any> {
            val First_Name = first_name.text.toString()
            val Last_Name = last_name.text.toString()
            val Email = email.text.toString()
            val Password = password.text.toString()
            val Confirm_Password = confirm_password.text.toString()
                if(First_Name.isEmpty() && Last_Name.isEmpty() && Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty())
                {
                    first_name.error="please fill this field with valid first name having length of greater than 2"
                    last_name.error="please fill this field with valid last name having length of greater than 2"
                    email.error="please fill this field with valid email"
                    password.error="please fill this field with length of minimum 8"
                    confirm_password.error="please fill this field with length of minimum 8"
                }
                else if(First_Name.length<=2)
                {
                    first_name.error="FirstName must be equal or greater than 3 characters"
                }
               else if(Last_Name.isEmpty() && Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty())
                {
                    last_name.error="please fill this field with valid last name having length of greater than 2"
                    email.error="please fill this field with valid email"
                    password.error="please fill this field with length of minimum 8"
                    confirm_password.error="please fill this field with length of minimum 8"
                }
                else if(Last_Name.length<=2)
                {
                    last_name.error="LastName must be equal or greater than 3 characters"
                }
                else if(Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty())
                {
                    email.error="please fill this field with valid email"
                    password.error="please fill this field with length of minimum 8"
                    confirm_password.error="please fill this field with length of minimum 8"
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    email.error="invalid email"
                }
                else if(Password.isEmpty() && Confirm_Password.isEmpty())
                {
                    password.error="please fill this field with length of minimum 8"
                    confirm_password.error="please fill this field with length of minimum 8"
                }
                else if(Password.length<8)
                {
                    password.error="please fill this field with length of minimum 8"
                }
                else if(Confirm_Password.isEmpty())
                {
                    confirm_password.error="please fill this field with length of minimum 8"
                }
                else if(Password != Confirm_Password)
                {
                    confirm_password.error="please enter the same password as above"
                }
                else {
                    mAuth!!.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener() { task: Task<AuthResult?> ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    requireActivity(),
                                    "Registration failed! try agian.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                Log.v("error", task.exception!!.message!!)

                            } else {
                                Toast.makeText(requireActivity(), "your account has been created!", Toast.LENGTH_LONG).show()
                                db_User = FirebaseDatabase.getInstance()
                                db_ref = db_User!!.getReference("CustomUsers")
                                val id = mAuth!!.currentUser!!.uid
                                db_ref!!.child(id).child("firstname").setValue(First_Name)
                                db_ref!!.child(id).child("lastname").setValue(Last_Name)
                                db_ref!!.child(id).child("email").setValue(Email)
                                db_ref!!.child(id).child("password").setValue(Password)
                               val imageData=Sharepref.getString( requireActivity(), Constants.CURRENT_USER_URI,"")
                                db_ref!!.child(id).child("photo").setValue(imageData)
                                Sharepref.setBoolean(requireActivity(), Constants.IS_REGISTER, true)

                                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)

                            }
                        }
                        .addOnFailureListener { e: Exception ->
                            Toast.makeText(
                                requireActivity(),
                                "invalid login credentials",
                                Toast.LENGTH_SHORT
                            ).show()


                        }
                }
        }

    }

    private fun uploadimage(path:Uri){
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
                Sharepref.setString( requireActivity(), Constants.CURRENT_USER_URI,  downloadUri.toString())
            } else {
                // Handle failures
                // ...
            }
        }
    }
}






