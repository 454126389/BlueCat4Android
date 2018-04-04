package com.tongge.ui.login.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by DZ on 2017/5/23.
 *
 */
class LoginPageAdapter(fm: FragmentManager,
                       fragments: List<Fragment>) : FragmentPagerAdapter(fm){
    private var fragments: List<Fragment>? = null
    init {
        this.fragments = fragments.filterNotNull()
        assert(this.fragments!!.size > 0)
    }

    override fun getItem(p0: Int): Fragment {
        return fragments!![p0]
    }

    override fun getCount(): Int {
        return fragments!!.size
    }
}