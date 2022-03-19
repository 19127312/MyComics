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

class TabPageAdapter(activity: FragmentActivity, private val tabCount:Int) : FragmentStateAdapter(activity){
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    lateinit var user: FirebaseUser
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        var type:String

        return when(position){
            0-> HomeFragment( getType())
            1-> RankingFragment()
            2-> ProfileFragment()
            else->HomeFragment( getType())
        }
    }
    fun getType():String{
        var result:String=""
        databaseReference?.child(user?.uid!!)?.child("Type")?.get()
            ?.addOnSuccessListener {
                result=it.value.toString()
            }
        return result
    }

}