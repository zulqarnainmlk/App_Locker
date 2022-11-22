package fragments

import adapters.AppsAdapter
import adapters.VaultAdapter
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_locker.R
import database.VaultDatabase
import helper.Constants
import helper.Sharepref
import kotlinx.android.synthetic.main.fragment_vault.*
import kotlinx.android.synthetic.main.fragment_vault.apps_tab
import kotlinx.android.synthetic.main.fragment_vault.home_tab
import kotlinx.android.synthetic.main.fragment_vault.profile_tab
import kotlinx.android.synthetic.main.fragment_vault.progressBar
import kotlinx.android.synthetic.main.fragment_vault.title
import kotlinx.android.synthetic.main.fragment_vault.vault_tab
import kotlinx.android.synthetic.main.fragment_vault.view_back
import kotlinx.coroutines.launch
import listeners.AdapterListener
import models.AppList
import models.DbVault

class VaultFragment : Fragment(), View.OnClickListener, AdapterListener {

    private val dbList = ArrayList<DbVault>()

    private val apps = ArrayList<AppList>()
    private val displayAppsList = ArrayList<AppList>()
    private val displayVaultList = ArrayList<DbVault>()

    var adapterSearch: AppsAdapter? = null




    private val appsAdapter by lazy { AppsAdapter(requireContext(), this, displayAppsList) }
    private val vaultAdapter by lazy { VaultAdapter(requireContext(), this, displayVaultList) }
    private val vaultDatabase by lazy { VaultDatabase.getDatabase(requireContext()).vaultDao() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vault, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            getVault()
            init()
            progressBar.visibility=View.GONE
        }, Constants.DELAY_TIME.toLong())

        title.text = getString(R.string.vault)
    }

    private fun init() {
        listeners()
        setUpSearch()
        allAppsRecyclerView()
        vaultAppRecyclerView()
        byDefaultDataShow()
        defaultView()

    }

    private fun setUpSearch() {
        searchApps.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filterApps(s.toString())
                filterVault(s.toString())

            }

        })
    }





    private fun defaultView() {
        tab_apps.setHintTextColor(resources.getColor(R.color.color_green))
        apps_underline.setBackgroundColor(resources.getColor(R.color.color_green))
        searchApps.visibility = View.VISIBLE
        apps_recycler.visibility = View.VISIBLE
    }

    private fun listeners() {
        tab_apps.setOnClickListener(this)
        tab_vault.setOnClickListener(this)
        view_back.setOnClickListener(this)
        home_tab.setOnClickListener(this)
        apps_tab.setOnClickListener(this)
        vault_tab.setOnClickListener(this)
        profile_tab.setOnClickListener(this)

    }
    private fun filterApps(text: String) {
        val filteredList: ArrayList<AppList> = ArrayList()
        for(item in apps)
        {
            if (item.appName!!.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        displayAppsList.clear()
        displayAppsList.addAll(filteredList)
        apps_recycler.adapter = appsAdapter
        appsAdapter.notifyDataSetChanged()
    }
    private fun filterVault(text: String) {

        val filteredVaultList: ArrayList<DbVault> = ArrayList()

        for (item in dbList) {
            if (item.name!!.toLowerCase().contains(text.toLowerCase())) {
                filteredVaultList.add(item)
            }
        }
        displayVaultList.clear()
        displayVaultList.addAll(filteredVaultList)

        vault_recycler.adapter = vaultAdapter
        vaultAdapter.notifyDataSetChanged()
    }

    private fun allAppsRecyclerView() {
        apps_recycler.layoutManager = LinearLayoutManager(requireContext())
        apps_recycler.adapter = appsAdapter
        //adapter=appsAdapter


    }

    private fun vaultAppRecyclerView() {
        vault_recycler.layoutManager = LinearLayoutManager(requireContext())
        vault_recycler.adapter = vaultAdapter
    }


    private fun openAllApps() {
        tab_apps.setHintTextColor(resources.getColor(R.color.color_green))
        apps_underline.setBackgroundColor(resources.getColor(R.color.color_green))
        tab_vault.setHintTextColor(resources.getColor(R.color.color_black))
        vault_underline.setBackgroundColor(resources.getColor(R.color.color_white))
        vault_recycler.visibility = View.GONE
        searchApps.visibility = View.VISIBLE
        apps_recycler.visibility = View.VISIBLE

        appsAdapter.notifyDataSetChanged()
    }

    private fun openVault() {
        getVault()
        tab_apps.setHintTextColor(resources.getColor(R.color.color_black))
        apps_underline.setBackgroundColor(resources.getColor(R.color.color_white))
        tab_vault.setHintTextColor(resources.getColor(R.color.color_green))
        vault_underline.setBackgroundColor(resources.getColor(R.color.color_green))
        apps_recycler.visibility = View.GONE
        searchApps.visibility = View.VISIBLE
        vault_recycler.visibility = View.VISIBLE
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun getAllApps() {
        apps.clear()
        displayAppsList.clear()
        val packs = requireActivity().packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            if (!isSystemPackage(p)) {
                val appName =
                    p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)
                val packages = p.applicationInfo.packageName
               val list= dbList.filter { it.pkgInfo == packages }

                Log.e("tag123","value "+list.size)


                if (list.isNotEmpty()) {
                    apps.add(AppList(true, appName, packages, icon))
                    displayAppsList.add(AppList(true, appName, packages, icon))
                } else {
                    apps.add(AppList(false, appName, packages, icon))
                    displayAppsList.add(AppList(false, appName, packages, icon))
                }


            }

        }


    }


    private fun byDefaultDataShow() {


        openAllApps()
        getAppsList()

    }


    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun adapterVault(key: String, position: Int) {
        when (key) {
            "unblock" -> {
                dbList[position].status = !dbList[position].status!!
                Log.e("block_view", "name  :${dbList[position].name}")
                Log.e("block_view", "block status   :${dbList[position].status}")
                val getId = Sharepref.getInt(requireContext(), "app_id", 0)
                lifecycleScope.launch {
                    vaultDatabase.specificItemRemove(dbList[position].pkgInfo!!)
                    vaultAdapter.notifyDataSetChanged()

                }
            }


        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getVault() {
        lifecycleScope.launch {

            vaultDatabase.getData().collect { data ->

                Log.e("tsg1435", "size ${dbList.size}")
                if (data.isNotEmpty()) {

                    dbList.clear()
                    displayVaultList.clear()
                    dbList.addAll(data)
                    displayVaultList.addAll(data)
                    vaultAdapter.notifyDataSetChanged()
                } else {
                    dbList.clear()
                    displayVaultList.clear()
                    vaultAdapter.notifyDataSetChanged()
                }
            }
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getAppsList() {
        lifecycleScope.launch {

            vaultDatabase.getData().collect { data ->

                getAllApps()
                appsAdapter.notifyDataSetChanged()
                Log.e("data", "data isnot available")

            }

        }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tab_apps -> {
                openAllApps()
            }
            R.id.tab_vault -> {
                openVault()

            }
            R.id.view_back -> {
                findNavController().navigate(R.id.action_vaultFragment_to_homeFragment)

            }
            R.id.home_tab -> {
                findNavController().navigate(R.id.action_vaultFragment_to_homeFragment)
            }
            R.id.apps_tab -> {
                findNavController().navigate(R.id.action_vaultFragment_to_appsFragment)

            }
            R.id.vault_tab -> {
                Toast.makeText(requireContext(), "vault", Toast.LENGTH_SHORT).show()

            }
            R.id.profile_tab -> {
                findNavController().navigate(R.id.action_vaultFragment_to_profileFragment)

            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun adapterData(key: String, position: Int) {
        Log.e("block_view", "key :$key")
        when (key) {
            "block" -> {
                if(Sharepref.getString(requireContext(),Constants.PIN_GENERATED ,"")!!.isEmpty()) {


                    // in case pin code is empty

                    apps[position].status = !apps[position].status
                    displayAppsList[position].status = !displayAppsList[position].status
                    Log.e("block_view", "name  :${apps[position].appName}")
                    Log.e("block_view", "block status   :${apps[position].status}")
                    val AppId = System.currentTimeMillis().toInt()

                    val data = DbVault(
                        AppId, displayAppsList[position].status, displayAppsList[position].appName,
                        displayAppsList[position].packageName, displayAppsList[position].icon.toString()
                    )


                    vaultAdapter.notifyDataSetChanged()



                    lifecycleScope.launch {
                        vaultDatabase.addData(data)

                    }
                    findNavController().navigate(R.id.action_vaultFragment_to_pinGenerationFragment)

                }
                else
                {

                    // in case pin code is not empty


                    apps[position].status = !apps[position].status
                    displayAppsList[position].status = !displayAppsList[position].status
                    Log.e("block_view", "name  :${apps[position].appName}")
                    Log.e("block_view", "block status   :${apps[position].status}")
                    val AppId = System.currentTimeMillis().toInt()

                    val data = DbVault(
                        AppId, displayAppsList[position].status, displayAppsList[position].appName,
                        displayAppsList[position].packageName, displayAppsList[position].icon.toString()
                    )


                    vaultAdapter.notifyDataSetChanged()



                    lifecycleScope.launch {
                        vaultDatabase.addData(data)

                    }
                }
            }
            "unblock" -> {

                displayAppsList[position].status = !displayAppsList[position].status
                Log.e("block_view", "name  :${apps[position].appName}")
                Log.e("block_view", "block status   :${apps[position].status}")
                val getId = Sharepref.getInt(requireContext(), "app_id", 0)
                lifecycleScope.launch {
                    vaultDatabase.specificItemRemove(displayAppsList[position].packageName)
                    vaultAdapter.notifyDataSetChanged()
                }
            }


        }
    }


}














