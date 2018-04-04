package com.tongge.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by DZ on 2017/5/25.
 *
 */
class MainPagerAdatper(fm: FragmentManager,
                       fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    var fragments: ArrayList<Fragment>? = null
    init {
        this.fragments = fragments
    }

    override fun getItem(p0: Int): Fragment {
       return fragments?.get(p0)!!
    }

    override fun getCount(): Int {
        return fragments?.size!!
    }

}