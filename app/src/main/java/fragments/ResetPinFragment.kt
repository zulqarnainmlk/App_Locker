package fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.seedlotfi.towerinfodialog.TowerDialog
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_reset_pin.*
import listeners.HomeListener


class ResetPinFragment : Fragment(), View.OnClickListener {
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
            newTitle = "Reset PIN"


        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_pin, container, false)
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
    private fun listeners() {
        reset_pin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.reset_pin ->
            {
                Toast.makeText(context,"Button Pressed", Toast.LENGTH_SHORT).show()
                if(current_pin.value.isEmpty() )
                {
                    Toast.makeText(context,"please enter a valid pin", Toast.LENGTH_SHORT).show()

                }
                else if(current_pin.value.length<6)
                {
                    Toast.makeText(context,"please re_enter your current PIN", Toast.LENGTH_SHORT).show()

                }
               else if(current_pin.value!=Sharepref.getString(requireContext(),Constants.PIN_GENERATED,""))
                {
                    Toast.makeText(context,"current pin is invalid", Toast.LENGTH_SHORT).show()

                }
                else if(new_pin.value.isEmpty() ||  new_pin.value.length<6)
                {
                    Toast.makeText(context,"please re_enter your new PIN", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Sharepref.setString(requireContext(), Constants.PIN_GENERATED,new_pin.value)
                    val dialog = TowerDialog.Companion.Builder()
                        // set context required
                        .setContext(requireContext())
                        .setTitle("Good Job!")
                        .setIsSuccess(true)
                        .setMessage(" PIN is upgraded.")
                        // set title bold default is false
                        .setTileIsBold(true)
                        .setButtonText("Done")
                        .build()

                    dialog.show {  }
                    findNavController().navigate(R.id.action_resetPinFragment_to_profileFragment)

                }
            }

        }
    }

}