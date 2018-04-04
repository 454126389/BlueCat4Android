package com.tongge.ui.login.views.fragments

import com.tongge.common.sysUtils.STLog
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.databinding.FragmentFindBinding
import com.tongge.ui.login.model.UserBean
import com.tongge.ui.login.views.activity.LoginActivity

class FindFragment : BaseFragment() {

    override fun getContentView(): Int {
        return R.layout.fragment_find
    }

    companion object {
        fun newInstance(): FindFragment {
            val fragment = FindFragment()
            return fragment
        }
    }

    override fun initFLayout() {
        var user = UserBean()
        val binding = dataBinding as FragmentFindBinding
        val userEvent = (activity as LoginActivity).userEvent
        userEvent?.findPwdUser = user
        binding.user = user
        binding.userEvent = userEvent
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }
}
