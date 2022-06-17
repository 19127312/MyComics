package com.khtn.ratevid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khtn.ratevid.fragment.*
import com.khtn.ratevid.model.userItem

class TabPageProfileAdapter(activity: FragmentManager, private val tabCount:Int) : FragmentStatePagerAdapter(activity){



    override fun getCount(): Int {
        return tabCount

    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0-> BlankBFragment()
            1-> BlankAFragment()

            else-> BlankBFragment()
        }
    }


}