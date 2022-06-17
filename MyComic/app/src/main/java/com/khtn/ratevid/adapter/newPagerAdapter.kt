package com.khtn.ratevid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class newPagerAdapter(sFM: FragmentManager):FragmentPagerAdapter(sFM) {
    private val FragmentList= ArrayList<Fragment>()
    private  val FragmentTitle= ArrayList<String>()
    override fun getCount(): Int {
        return FragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return FragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return FragmentTitle[position]
    }
    fun addFragment(fm:Fragment,title:String){
        FragmentList.add(fm)
        FragmentTitle.add(title)
    }

}