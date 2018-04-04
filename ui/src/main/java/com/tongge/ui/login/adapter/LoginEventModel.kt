package com.tongge.ui.login.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.widget.Button
import com.tongge.netCore.audio.AudioPlayTask
import com.tongge.netCore.audio.RecordTask
import com.tongge.ui.login.model.UserBean
import com.tongge.ui.utils.shortToast

/**
 * Created by DZ on 2017/5/25.
 *
 */
class LoginEventModel {
    companion object {
        val LOGIN = 0x001
        val RESIGTER = 0x002
        val FINDPWD = 0x003
    }

    var loginUser: UserBean? = null
    var registerUser: UserBean? = null
    var findPwdUser: UserBean? = null
    var pagerControl: ((pagerCount: Int) -> Unit)? = null
    var listener: LoginListener? = null
    private var codeBtn: Button? = null
    private var animator: ValueAnimator? = null
    private var codeStr = ""

    fun login(view: View) {
        listener?.requestSucc(LOGIN)
//        STHudView.show(view.rootView, false)
//        BCUserManager.shareInstance().user = loginUser
//        BCUserManager.shareInstance().login(object : NetCoreBaseCallBack(){
//            override fun callback_servers(status: Int) {
//                STLog.d("login result","$status")
//                if (status == NetCoreResultParams.BCS_STATUS_CODE.BSTA_MNG_SUCCESS)
//                    listener?.requestSucc(LOGIN)
//                else
//                    listener?.requestFailed(LOGIN, status)
//
//            }
//        })
    }

    fun register(view: View) {
        RecordTask.openSpeak()
        shortToast(view.context, "register")
    }

    fun findPwd(view: View) {
        RecordTask.closeSpeak()
        AudioPlayTask.openSound()
        shortToast(view.context, "findPwd")
    }

    fun requestCode(view: View) {
        codeBtn = view as Button
        codeStr = codeBtn?.text.toString()
        countAnima()
        shortToast(view.context, "requestCode")
    }

    fun switchPager(pagerCount: Int) {
        if (pagerCount == 1) {
            animator?.end()
            codeBtn?.text = codeStr
        }
        codeBtn = null
        animator = null
        pagerControl?.invoke(pagerCount)
    }

    private fun countAnima() {
        animator = ValueAnimator.ofInt(60, 0).apply {
            duration = 1000 * 60
            interpolator = null
            addUpdateListener {
                val i = animatedValue as Int
                codeBtn?.text = "$i s"
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    codeBtn?.text = codeStr
                    animator = null
                }
            })
            start()
        }
    }

    interface LoginListener {
        fun requestSucc(type: Int)
        fun requestFailed(type: Int, err_code: Int)
    }

}