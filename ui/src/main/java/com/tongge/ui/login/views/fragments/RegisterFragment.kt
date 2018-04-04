package com.tongge.ui.login.views.fragments

import com.tongge.common.sysUtils.STLog
import com.tongge.ui.databinding.FragmentRegisterBinding
import com.tongge.ui.login.model.UserBean
import com.tongge.ui.login.views.activity.LoginActivity

class RegisterFragment : com.tongge.ui.base.BaseFragment() {

    override fun getContentView(): Int {
        return com.tongge.ui.R.layout.fragment_register
    }

    companion object {
        fun newInstance(): com.tongge.ui.login.views.fragments.RegisterFragment {
            val fragment = com.tongge.ui.login.views.fragments.RegisterFragment()
            return fragment
        }
    }

    override fun initFLayout() {
        var user = UserBean()
        val binding = dataBinding as FragmentRegisterBinding
        val userEvent = (activity as LoginActivity).userEvent
        binding.user = user
        userEvent?.registerUser = user
        binding.userEvent = userEvent
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }
}