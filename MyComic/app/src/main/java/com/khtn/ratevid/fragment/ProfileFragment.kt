package com.khtn.ratevid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.newPagerAdapter
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment(user: userItem) : Fragment() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    lateinit var user: FirebaseUser
    var curUser:userItem=user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val x: View = inflater.inflate(R.layout.fragment_profile,container, false)
        val myViewPager:ViewPager= x.findViewById<ViewPager>(R.id.viewPagerProfile)
        val myTabLayout:TabLayout= x.findViewById<TabLayout>(R.id.tabsProfile)
        if(myViewPager!=null){
            setupViewPager(myViewPager)
        }
        assert(myViewPager != null)
        myTabLayout.setupWithViewPager(myViewPager)
        myTabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_home_selected)
        myTabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_ranking_selected)
        myTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {

                myViewPager.currentItem=tab.position
                when(tab.position){
                    0->tab.setIcon(R.drawable.ic_home_selected)
                    1->tab.setIcon(R.drawable.ic_ranking_selected)
                }

                //Log.d("MyScreen",tab.position.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var oldPos=tab?.position
                when(oldPos){
                    0->tab?.setIcon(R.drawable.ic_home)
                    1->tab?.setIcon(R.drawable.ic_ranking)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        return x
    }

    private fun setupViewPager(myViewPager: ViewPager) {
        val newAdapter= newPagerAdapter(childFragmentManager )
        newAdapter.addFragment(BlankAFragment(),"A")
        newAdapter.addFragment(BlankBFragment(),"B")
        myViewPager.adapter=newAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setUpTabBar()

    }


    private fun setUpTabBar() {
        val newAdapter= newPagerAdapter(childFragmentManager )
        newAdapter.addFragment(BlankAFragment(),"A")
        newAdapter.addFragment(BlankBFragment(),"B")
        //newAdapter.addFragment(HomeFragment(user),"home")
        viewPagerProfile.adapter=newAdapter
        tabsProfile.setupWithViewPager(viewPagerProfile)

        tabsProfile.getTabAt(0)!!.setIcon(R.drawable.ic_home_selected)
        tabsProfile.getTabAt(1)!!.setIcon(R.drawable.ic_ranking_selected)
        tabsProfile.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPagerProfile.currentItem=tab.position
                when(tab.position){
                    0->tab.setIcon(R.drawable.ic_home_selected)
                    1->tab.setIcon(R.drawable.ic_ranking_selected)
                }

                //Log.d("MyScreen",tab.position.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var oldPos=tab?.position
                when(oldPos){
                    0->tab?.setIcon(R.drawable.ic_home)
                    1->tab?.setIcon(R.drawable.ic_ranking)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }
}