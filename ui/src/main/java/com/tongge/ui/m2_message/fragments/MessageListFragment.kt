package com.tongge.ui.m2_message.fragments

import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment

class MessageListFragment : BaseFragment() {

    override fun getContentView(): Int {
        return R.layout.fragment_message_list
    }

    companion object {
        fun newInstance(): MessageListFragment {
            val fragment = MessageListFragment()
            return fragment
        }
    }

    override fun initFLayout() {
    }
}
