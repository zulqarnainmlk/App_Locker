package fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.app_locker.R
import kotlinx.android.synthetic.main.fragment_privacy.*
import listeners.HomeListener


class PrivacyFragment : Fragment() {
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
            newTitle = getString(R.string.privacy_policy)


        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init()
    {
        privacy_web_view.webViewClient = WebViewClient()
        privacy_web_view.loadUrl("https://www.zeentechnologies.com/applocker/privacypolicy")
        privacy_web_view.settings.javaScriptEnabled = true
    }


}