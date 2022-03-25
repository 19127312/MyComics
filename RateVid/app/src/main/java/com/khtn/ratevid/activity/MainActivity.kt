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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*


class MainActivity : AppCompatActivity() {

    lateinit var typeUser:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        typeUser = intent.getStringExtra("typeuser").toString()

        setUpTabBar()
    }

    private fun setUpTabBar() {


        val adapter= TabPageAdapter(this,tabLayout.tabCount,typeUser)
        viewPager.adapter=adapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPager.currentItem=tab.position
                when(tab.position){
                    0->tab.setIcon(R.drawable.ic_home_selected)
                    1->tab.setIcon(R.drawable.ic_ranking_selected)
                    2->tab.setIcon(R.drawable.ic_profile_selected)
                }

                //Log.d("MyScreen",tab.position.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var oldPos=tab?.position
                when(oldPos){
                    0->tab?.setIcon(R.drawable.ic_home)
                    1->tab?.setIcon(R.drawable.ic_ranking)
                    2->tab?.setIcon(R.drawable.ic_profile)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

}