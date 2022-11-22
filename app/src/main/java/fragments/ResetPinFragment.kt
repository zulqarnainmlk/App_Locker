package fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.seedlotfi.towerinfodialog.TowerDialog
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_reset_pin.*


class ResetPinFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text=getString(R.string.reset)
        init()
    }
    private fun init() {
        listeners()
    }

    private fun listeners() {
        reset_pin.setOnClickListener(this)
        view_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.view_back ->
        {
            findNavController().navigate(R.id.action_resetPinFragment_to_profileFragment)
        }
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