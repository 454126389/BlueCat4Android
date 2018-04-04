package com.tongge.ui.login.views.activity

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.view.WindowManager
import android.widget.ImageView
import com.tongge.ui.R

class AppLoadingActivity : com.tongge.ui.base.BaseActivity() {

    private var logoImg: ImageView? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        window.setBackgroundDrawable(null);
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val myBind: com.tongge.ui.databinding.ActivityAppLoadingBinding =
                android.databinding.DataBindingUtil.setContentView(this, R.layout.activity_app_loading)
        val appLoadImg = myBind.appLoadBg
        logoImg = myBind.appLoadImg
//        appLoadImg.setImageDrawable(resources.getDrawable(R.mipmap.ic_launcher_round))
//        appLoadImg.postDelayed({
//            val intent = android.content.Intent(this,
//                    LoginActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
//            finish()
//        },2000)

//        NetworkIntentService.startActionForNetworkLinsener(this)

    }

    override fun onResume() {
        super.onResume()
        logoAnim()
    }

    fun logoAnim() {
        val anim = AnimatorInflater.loadAnimator(this, R.animator.app_loading_anim)
        anim.setTarget(logoImg)
        val context = this
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                val intent = android.content.Intent(context,
                        LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        })
        anim.start()
    }

}
