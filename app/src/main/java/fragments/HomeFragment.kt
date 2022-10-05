package fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuAdapter
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.app_locker.R
import kotlinx.android.synthetic.main.fragment_home.*
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import java.util.*


class HomeFragment : Fragment(){



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        auth=FirebaseAuth.getInstance()
//        auth.signOut()
//        val args =this.arguments
//        val gmail=args?.get("email")
//        val gname=args?.get("name")
//
//        display.text=gmail.toString() + "\n" +gname.toString()
        //(activity as AppCompatActivity)
        apps_card.setOnClickListener {
            val fragment = AppsFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.container_apps, fragment)?.commit()
        }
    }


}