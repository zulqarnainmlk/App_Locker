package adapters

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
//import fragments.blockfragment.BlockFragment
//import fragments.blockfragment.BlockListFragment
//import fragments.blockfragment.UnblockListFragment

class ViewAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm)  {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
               // return UnblockListFragment()
            }
            1 -> {
             //   return BlockListFragment()
            }

            else -> Toast.makeText(myContext,"fragment not found",Toast.LENGTH_SHORT).show()
        }
        return Fragment()
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }


}