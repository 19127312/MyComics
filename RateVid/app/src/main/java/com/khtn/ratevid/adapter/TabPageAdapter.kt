package com.khtn.ratevid.adapter

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.khtn.ratevid.fragment.HomeFragment
import com.khtn.ratevid.fragment.ProfileFragment
import com.khtn.ratevid.fragment.RankingFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount:Int,type: String) : FragmentStateAdapter(activity){
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    lateinit var user: FirebaseUser
    var typeUser=type
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {


        return when(position){
            0-> HomeFragment( typeUser)
            1-> RankingFragment()
            2-> ProfileFragment()
            else->HomeFragment( typeUser)
        }
    }


}