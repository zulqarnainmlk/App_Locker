package fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.app_locker.R
import helper.Constants
import helper.Sharepref


class SplashFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view= inflater.inflate(R.layout.fragment_splash, container, false)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if(Sharepref.getBoolean(requireActivity(),Constants.IS_GMAIL_LOGIN,false))
        {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
             Log.e("LoginBy:","GMAIL_LOGIN")
            },Constants.SPLASH_TIME.toLong())
        }
       else if(Sharepref.getBoolean(requireActivity(),Constants.IS_LOGIN,false))
        {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                Log.e("LoginBy:","CUSTOM_LOGIN")

            },Constants.SPLASH_TIME.toLong())
        }
       else if(Sharepref.getBoolean(requireActivity(),Constants.IS_REGISTER,false))
        {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                Log.e("LoginBy:","REGISTRATION_LOGIN")

            },Constants.SPLASH_TIME.toLong())
        }
        else
        {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_signupFragment)
            }, Constants.SPLASH_TIME.toLong())
        }

    }


}