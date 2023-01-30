package fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide.init
import com.example.app_locker.R
import helper.Constants
import helper.Sharepref
import listeners.HomeListener


class SplashFragment : Fragment() {
    private lateinit var homeListener: HomeListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }
    override fun onResume() {
        super.onResume()
        homeListener.onHomeDataChangeListener(
            toolbarVisibility = false,
            backBtnVisibility = false,
            newTitle = ""


        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        if (Sharepref.getBoolean(requireActivity(), Constants.IS_GMAIL_LOGIN, false)) {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                Log.e("LoginBy:", "GMAIL_LOGIN")
            }, Constants.SPLASH_TIME.toLong())
        } else if (Sharepref.getBoolean(requireActivity(), Constants.IS_LOGIN, false)) {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                Log.e("LoginBy:", "CUSTOM_LOGIN")

            }, Constants.SPLASH_TIME.toLong())
        } else if (Sharepref.getBoolean(requireActivity(), Constants.IS_SIGNUP, false)) {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                Log.e("LoginBy:", "REGISTRATION_LOGIN")

            }, Constants.SPLASH_TIME.toLong())
        } else {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                Log.e("LoginBy:", "SIGNUP")

            }, Constants.SPLASH_TIME.toLong())
        }
    }


}