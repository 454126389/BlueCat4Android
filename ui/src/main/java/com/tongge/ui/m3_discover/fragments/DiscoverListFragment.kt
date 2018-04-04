package com.tongge.ui.m3_discover.fragments

import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment

class DiscoverListFragment : BaseFragment() {

    override fun getContentView(): Int {
        return R.layout.fragment_discover_list
    }

    companion object {
        fun newInstance(): DiscoverListFragment {
            val fragment = DiscoverListFragment()
            return fragment
        }
    }

    override fun initFLayout() {

    }
}
