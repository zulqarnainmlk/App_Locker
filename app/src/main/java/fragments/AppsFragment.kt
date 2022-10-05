package fragments

import adapters.AppsListAdapter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_locker.R
import models.AppInfo
import android.content.pm.PackageManager





class AppsFragment : Fragment() {
    private var installedAppAdapter: AppsListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview =view.findViewById<RecyclerView>(R.id.installed_app_list)
        val editText=view.findViewById<EditText>(R.id.edittext)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        // code for recycler
        recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        val apps  = ArrayList<AppInfo>()

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
        recyclerview.adapter = installedAppAdapter
        //code for count
        val abc: String = installedAppAdapter!!.itemCount.toString() + ""
        val countApps = view.findViewById<View>(R.id.countApps) as TextView
        countApps.text = "Total Installed Apps: $abc"
        Toast.makeText(requireActivity(), "$abc Apps", Toast.LENGTH_SHORT).show()


    }
    private fun filter(text: String) {
        val filteredList  = ArrayList<AppInfo>()
        for (item in filteredList) {
            if (item.appName.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        installedAppAdapter!!.filterList(filteredList)
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


}