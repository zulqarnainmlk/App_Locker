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

import kotlinx.android.synthetic.main.fragment_pin_generation.*
import kotlinx.android.synthetic.main.fragment_pin_generation.cn_pinview
import kotlinx.android.synthetic.main.fragment_pin_generation.pinview
import kotlinx.android.synthetic.main.fragment_pin_generation.setPin
import kotlinx.android.synthetic.main.fragment_pin_generation.title
import kotlinx.android.synthetic.main.fragment_pin_generation.view_back


class PinGenerationFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_generation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text=getString(R.string.generate_pin)
        init()
    }
    private fun init() {
        listeners()
    }

    private fun listeners() {
        setPin.setOnClickListener(this)
        view_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.view_back ->
        {
            findNavController().navigate(R.id.action_pinGenerationFragment_to_vaultFragment)
        }
            R.id.setPin ->
            {
                Toast.makeText(context,"Button Pressed", Toast.LENGTH_SHORT).show()
                if(pinview.value.isEmpty() )
                {
                    Toast.makeText(context,"please enter a valid pin", Toast.LENGTH_SHORT).show()

                }
                else if(pinview.value.length<6 &&  cn_pinview.value.isEmpty())
                {
                    Toast.makeText(context,"please re_enter your PIN", Toast.LENGTH_SHORT).show()

                }
                if(cn_pinview.value.isEmpty() )
                {
                    Toast.makeText(context,"please confirm your pin", Toast.LENGTH_SHORT).show()

                }
                else if(pinview.value!=cn_pinview.value || cn_pinview.value.length<6)
                {
                    Toast.makeText(context,"pin is not same!", Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Sharepref.setString(requireContext(), Constants.PIN_GENERATED,pinview.value)
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
                    findNavController().navigate(R.id.action_pinGenerationFragment_to_vaultFragment)

                }
            }

        }
    }


}