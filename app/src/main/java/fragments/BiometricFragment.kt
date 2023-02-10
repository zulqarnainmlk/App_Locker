package fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_biometric.*
import listeners.HomeListener
import java.util.concurrent.Executor


class BiometricFragment : Fragment(), View.OnClickListener {
    private lateinit var googleSignInClient: GoogleSignInClient
    var mAuth: FirebaseAuth? = null
    private var db_ref: DatabaseReference? = null
    private var db_User: FirebaseDatabase? = null
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var executor: Executor
    private lateinit var callBack: BiometricPrompt.AuthenticationCallback
    private var keyguardManager: KeyguardManager? = null
    private var biometricNotSet: Boolean = false

    companion object {
        const val RC_BIOMETRICS_ENROLL = 10
        const val RC_DEVICE_CREDENTIAL_ENROLL = 18
    }

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
            newTitle = "Pin and Biometric"


        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biometric, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        checkDeviceCanAuthenticateWithBiometrics()
    }

    private fun init() {

        Log.e("Tsf", "BiometricFragment")
        checkUser()
        defaultView()
        switchChecker()
        inAuth()
        listener()
    }

    private fun listener(){
        set_pin_view.setOnClickListener(this)
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
                    tvTitle.text = displayName

                    Log.e("Testing: ", firstName!! + "\n" + lastName!!)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })


        }
    }

    private fun defaultView() {
        Log.e(
            "switch1",
            Sharepref.getString(requireContext(), Constants.PIN_GENERATED, "").toString()
        )
        Log.e(
            "switch2",
            Sharepref.getString(requireContext(), Constants.SET_BIOMETRIC, "").toString()
        )


        if (Sharepref.getString(requireContext(), Constants.PIN_GENERATED, "")!!.isNotEmpty()) {
            switch1.isChecked = true
            switch2.isChecked = false

        } else if (Sharepref.getString(requireContext(), Constants.SET_BIOMETRIC, "")!!
                .isNotEmpty()
        ) {
            switch2.isChecked = true
            switch1.isChecked = false
        }
    }

    private fun switchChecker() {
        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.e(
                    "switch1",
                    Sharepref.getString(requireContext(), Constants.PIN_GENERATED, "").toString()
                )
                if (Sharepref.getString(requireContext(), Constants.PIN_GENERATED, "")!!
                        .isEmpty()
                ) {

                    Sharepref.setString(requireContext(), Constants.SET_BIOMETRIC, "")

                }

                switch2.isChecked = false
            } else {
                Sharepref.setString(requireContext(), Constants.PIN_GENERATED, "")

            }
        }
        switch2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.e(
                    "switch2",
                    Sharepref.getString(requireContext(), Constants.SET_BIOMETRIC, "").toString()
                )

                switch1.isChecked = false
                Sharepref.setString(requireContext(), Constants.SET_BIOMETRIC, "1")
                Sharepref.setString(requireContext(), Constants.PIN_GENERATED, "")


                if (biometricNotSet) {

                    switch2.isChecked = false
                    val builder = AlertDialog.Builder(requireContext())

                    with(builder)
                    {
                        setTitle("Alert!!")
                        setMessage(getString(R.string.error_no_device_credentials))
                        setIcon(R.drawable.ic_lock)
                        setPositiveButton("ok") { _, _ ->

                        }
                        show()
                    }
                } else {
                    authenticateWithBiometrics()
                }


            } else {
                Sharepref.setString(requireContext(), Constants.SET_BIOMETRIC, "")

            }
        }
    }

    private fun checkDeviceCanAuthenticateWithBiometrics() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_no_support_biometrics),
                    Toast.LENGTH_LONG
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_no_hardware_available),
                    Toast.LENGTH_LONG
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                checkAPILevelAndProceed()
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_security_update_required),
                    Toast.LENGTH_LONG
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_unknown),
                    Toast.LENGTH_LONG
                ).show()
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_unknown),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun inAuth() {
        executor = ContextCompat.getMainExecutor(requireContext())
        callBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_unknown),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_success),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(requireContext(), getErrorMessage(errorCode), Toast.LENGTH_LONG)
                    .show()
            }
        }
        biometricPrompt = BiometricPrompt(this, executor, callBack)
    }

    private fun biometricsEnrollIntent(): Intent {
        return Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
        }
    }

    private fun setUpDeviceLockInAPIBelow23Intent(): Intent {
        return Intent(Settings.ACTION_SECURITY_SETTINGS)
    }

    private fun checkAPILevelAndProceed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            startActivityForResult(setUpDeviceLockInAPIBelow23Intent(), RC_DEVICE_CREDENTIAL_ENROLL)
        } else {
            try {
                startActivityForResult(biometricsEnrollIntent(), RC_BIOMETRICS_ENROLL)
            } catch (e: Exception) {
                biometricNotSet = true
                Log.e("Error:", e.message.toString())
            }

        }
    }

    private fun getErrorMessage(errorCode: Int): String {
        return when (errorCode) {
            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                getString(R.string.message_user_app_authentication)
            }
            BiometricPrompt.ERROR_HW_UNAVAILABLE -> {
                getString(R.string.error_hw_unavailable)
            }
            BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> {
                getString(R.string.error_unable_to_process)
            }
            BiometricPrompt.ERROR_TIMEOUT -> {
                getString(R.string.error_time_out)
            }
            BiometricPrompt.ERROR_NO_SPACE -> {
                getString(R.string.error_no_space)
            }
            BiometricPrompt.ERROR_CANCELED -> {
                getString(R.string.error_canceled)
            }
            BiometricPrompt.ERROR_LOCKOUT -> {
                getString(R.string.error_lockout)
            }
            BiometricPrompt.ERROR_VENDOR -> {
                getString(R.string.error_vendor)
            }
            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                getString(R.string.error_lockout_permanent)
            }
            BiometricPrompt.ERROR_USER_CANCELED -> {
                getString(R.string.error_user_canceled)
            }
            BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                checkAPILevelAndProceed()
                getString(R.string.error_no_biometrics)
            }
            BiometricPrompt.ERROR_HW_NOT_PRESENT -> {
                getString(R.string.error_hw_not_present)
            }
            BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> {

                startActivityForResult(biometricsEnrollIntent(), RC_BIOMETRICS_ENROLL)
                getString(R.string.error_no_device_credentials)


            }
            BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED -> {
                getString(R.string.error_security_update_required)
            }
            else -> {
                getString(R.string.error_unknown)
            }
        }
    }

    private fun authenticateWithBiometrics() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(getString(R.string.title_biometric_dialog))
            setDescription(getString(R.string.text_description_biometrics_dialog))
            setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        }.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            keyguardManager =
                requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager?.let { manager ->
                if (manager.isKeyguardSecure) {
                    biometricPrompt.authenticate(promptInfo)
                } else {
                    startActivityForResult(
                        setUpDeviceLockInAPIBelow23Intent(),
                        RC_DEVICE_CREDENTIAL_ENROLL
                    )
                }
            }
        } else {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.set_pin_view -> {
                findNavController().navigate(R.id.action_biometricFragment_to_pinGenerationFragment)
            }
        }
    }

}