package com.tongge.ui.login.views.activity

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.tongge.common.customViews.STHudView
import com.tongge.common.sysUtils.STCheckPermissions
import com.tongge.common.sysUtils.STLog
import com.tongge.manager.user.BCUserManager
import com.tongge.netCore.protocol.NetCoreResultParams
import com.tongge.ui.R
import com.tongge.ui.base.BaseActivity
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.databinding.ActivityLoginBinding
import com.tongge.ui.login.adapter.LoginEventModel
import com.tongge.ui.login.adapter.LoginPageAdapter
import com.tongge.ui.login.model.UserBean
import com.tongge.ui.login.views.fragments.FindFragment
import com.tongge.ui.login.views.fragments.LoginFragment
import com.tongge.ui.login.views.fragments.RegisterFragment
import com.tongge.ui.main.MainActivity
import com.tongge.ui.utils.AppCommonParamsUtil
import com.tongge.ui.utils.shortToast
import com.tongge.ui.utils.slideSpeed

class LoginActivity : BaseActivity(), LoginEventModel.LoginListener, BaseFragment.OnFragmentInteractionListener {

    private var viewpage: ViewPager? = null
    var userEvent: LoginEventModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initData()
    }

    private fun initUI() {
        val myBind = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login) as ActivityLoginBinding
        STLog.e(this, ""+AppCommonParamsUtil.shareInstance().loginIcon)
        myBind.loginIcon.setBackgroundResource(AppCommonParamsUtil.shareInstance().loginIcon)
        viewpage = myBind.loginPage

        val fragments = listOf(RegisterFragment.newInstance(),
                LoginFragment.newInstance(),
                FindFragment.newInstance())
        val loginPageAdapter = LoginPageAdapter(supportFragmentManager, fragments)
        viewpage?.let {
            it.adapter = loginPageAdapter
            it.currentItem = 1
            it.offscreenPageLimit = fragments.size - 1
            it.setOnTouchListener({ v, event -> true })
            it.slideSpeed(4)
        }
    }

    fun initData() {
        var user = UserBean()
        userEvent = LoginEventModel()
        userEvent?.pagerControl = { pagerCount: Int -> pagerSwitch(pagerCount) }
        userEvent?.listener = this
        BCUserManager.shareInstance().user = user
    }

    fun pagerSwitch(pagerCount: Int) {
        if (viewpage != null) {
            viewpage?.setCurrentItem(pagerCount % 3, true)
        }
    }

    override fun requestSucc(type: Int) {
        when(type){
            LoginEventModel.LOGIN -> {
                STHudView.dismiss()
                startActivity(Intent(this,
                        MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                this.finish()
            }
            LoginEventModel.RESIGTER -> {

            }
            LoginEventModel.FINDPWD -> {

            }
            else -> {

            }
        }

    }

    override fun requestFailed(type: Int, err_code: Int) {
        when(type){
            LoginEventModel.LOGIN -> {
                STHudView.dismiss()
                if (err_code == NetCoreResultParams.BCS_STATUS_CODE.BSTA_LOGIN_FAILED)
                    shortToast(this, getString(R.string.login_failed))
                else
                    shortToast(this, getString(R.string.connect_server_failed))
            }
            LoginEventModel.RESIGTER -> {

            }
            LoginEventModel.FINDPWD -> {

            }
            else -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        STCheckPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onFragmentInteraction(token: String) {

    }
}
