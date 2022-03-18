package com.khtn.ratevid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khtn.ratevid.fragment.HomeFragment
import com.khtn.ratevid.fragment.ProfileFragment
import com.khtn.ratevid.fragment.RankingFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount:Int) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> HomeFragment()
            1-> RankingFragment()
            2-> ProfileFragment()
            else->HomeFragment()
        }
    }

}