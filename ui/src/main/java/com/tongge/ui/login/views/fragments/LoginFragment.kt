package com.tongge.ui.login.views.fragments

import com.tongge.manager.user.BCUserManager
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.databinding.FragmentLoginBinding
import com.tongge.ui.login.model.UserBean
import com.tongge.ui.login.views.activity.LoginActivity

class LoginFragment : BaseFragment() {

    override fun getContentView(): Int {
        return R.layout.fragment_login
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }

    override fun initFLayout() {
        val binding = dataBinding as FragmentLoginBinding
        var user = BCUserManager.shareInstance().user as UserBean
        user.username.set("17763693336")
        user.password.set("123123")
        val userEvent = (activity as LoginActivity).userEvent
        userEvent?.loginUser = user
        binding.user = user
        binding.userEvent = userEvent
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }
}
