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
import kotlinx.android.synthetic.main.fragment_pin_generation.*
import kotlinx.android.synthetic.main.fragment_pin_generation.setPin
import kotlinx.android.synthetic.main.fragment_pin_generation.tvTitle

import listeners.HomeListener


class PinGenerationFragment : Fragment(), View.OnClickListener {
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
            newTitle = "Generate PIN"


        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_generation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }
    private fun init() {
        currentUser()
        listeners()
    }

    private fun currentUser() {
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
        setPin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.setPin ->
            {
//                Toast.makeText(context,"Button Pressed", Toast.LENGTH_SHORT).show()
                if(new_gen_pin.value.isEmpty() )
                {
                    Toast.makeText(context,"please enter a valid pin", Toast.LENGTH_SHORT).show()

                }
                else if(new_gen_pin.value.length<6 )
                {
                    Toast.makeText(context,"please enter 6 digit lock pin", Toast.LENGTH_SHORT).show()

                }
                if(con_gen_pin.value.isEmpty() )
                {
                    Toast.makeText(context,"please confirm your pin", Toast.LENGTH_SHORT).show()

                }
                else if( con_gen_pin.value.length<6)
                {
                    Toast.makeText(context,"pin is not same", Toast.LENGTH_SHORT).show()

                }
                else if(new_gen_pin.value.toString()!=con_gen_pin.value.toString() )
                {
                    Toast.makeText(context,"pin is not same!", Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Sharepref.setString(requireContext(), Constants.PIN_GENERATED,new_gen_pin.value)
                    val dialog = TowerDialog.Companion.Builder()
                        // set context required
                        .setContext(requireContext())
                        .setTitle("Good Job!")
                        .setIsSuccess(true)
                        .setMessage(" PIN setup successful. ")
                        // set title bold default is false
                        .setTileIsBold(true)
                        .setButtonText("Done")
                        .build()

                    dialog.show {  }
                    findNavController().navigate(R.id.action_pinGenerationFragment_to_biometricFragment)

                }
            }

        }
    }


}