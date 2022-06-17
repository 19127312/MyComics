package com.khtn.ratevid.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.khtn.ratevid.R

import com.khtn.ratevid.adapter.TabPageAdapter
import com.khtn.ratevid.adapter.newPagerAdapter
import com.khtn.ratevid.fragment.HomeFragment
import com.khtn.ratevid.fragment.ProfileFragment
import com.khtn.ratevid.fragment.RankingFragment
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*


class MainActivity : AppCompatActivity() {
    lateinit var user:userItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        user= intent.getSerializableExtra("user") as userItem

        setUpTabBar()
    }

    private fun setUpTabBar() {
        val newAdapter= newPagerAdapter(supportFragmentManager)
        newAdapter.addFragment(HomeFragment(user),"home")
        newAdapter.addFragment(RankingFragment(user),"rank")
        newAdapter.addFragment(ProfileFragment(user),"profile")
        viewPager.adapter=newAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_home_selected)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_ranking)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_profile)

//        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab) {
//
//                viewPager.currentItem=tab.position
//                when(tab.position){
//                    0->tab.setIcon(R.drawable.ic_home_selected)
//                    1->tab.setIcon(R.drawable.ic_ranking_selected)
//                    2->tab.setIcon(R.drawable.ic_profile_selected)
//                }
//
//                //Log.d("MyScreen",tab.position.toString())
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                var oldPos=tab?.position
//                when(oldPos){
//                    0->tab?.setIcon(R.drawable.ic_home)
//                    1->tab?.setIcon(R.drawable.ic_ranking)
//                    2->tab?.setIcon(R.drawable.ic_profile)
//                }
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//
//        })

//        val adapter= TabPageAdapter(this,tabLayout.tabCount,user)
//        viewPager.adapter=adapter
//        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//
//                tabLayout.selectTab(tabLayout.getTabAt(position))
//            }
//        })
//
//
    }

}