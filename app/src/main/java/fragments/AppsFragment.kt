package fragments

import adapters.AppsListAdapter
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_locker.R
import database.VaultDatabase
import helper.Constants
import kotlinx.android.synthetic.main.fragment_apps.*

import kotlinx.android.synthetic.main.fragment_apps.progressBar
import listeners.HomeListener

import models.AppInfo
import java.util.*


class AppsFragment : Fragment()  {
    private var installedAppAdapter: AppsListAdapter? = null
    private val vaultDatabase by lazy { VaultDatabase.getDatabase(requireContext()).vaultDao() }
    private val apps  = ArrayList<AppInfo>()

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
            newTitle = getString(R.string.apps_title)


        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apps, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            init()
            progressBar.visibility=View.GONE
        }, Constants.DELAY_TIME.toLong())
        //title.text = "Apps"
    }
    private fun init() {
        listeners()
        setUpSearch()
        setUpRecycler()
    }

    private fun setUpSearch() {
        searchField.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())

            }

        })    }

    private fun listeners() {
//        searchField.setOnClickListener(this)

    }

    private fun filter(text: String) {

        val filteredList: ArrayList<AppInfo> = ArrayList()
        //val filteredList: ArrayList<ExampleItem> = ArrayList()

        for (item in apps) {
            if (item.appName.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }

        installedAppAdapter!!.filterList(filteredList)
//        for (item in apps) {
//            if (item.getText1().toLowerCase().contains(text.lowercase(Locale.getDefault()))) {
//                filteredList.add(item)
//            }
//        }
//        mAdapter.filterList(filteredList)
    }

    private fun setUpRecycler() {
        apps_list.layoutManager = LinearLayoutManager(requireActivity())
       // val apps  = ArrayList<AppInfo>()

        val packs = requireActivity().packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            if (!isSystemPackage(p)) {
                val appName = p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)
                val packages = p.applicationInfo.packageName
                apps.add(AppInfo(appName,packages, icon))
                installedAppAdapter = AppsListAdapter(apps)
            }
        }
        apps_list.adapter = installedAppAdapter
    }
    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

}