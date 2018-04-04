package com.tongge.ui.m1_video.activitys

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.databinding.ViewDataBinding
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.tongge.common.spUtils.SpUtil
import com.tongge.common.sysUtils.STCheckPermissions
import com.tongge.common.sysUtils.STLog
import com.tongge.netCore.cameraInfo.VideoPlayer
import com.tongge.netCore.protocol.NetCoreBaseCallBack
import com.tongge.netCore.protocol.NetCoreResultParams
import com.tongge.ui.R
import com.tongge.ui.base.BaseToorbarActivity
import com.tongge.ui.databinding.ActivityPlayerBinding
import com.tongge.ui.m1_video.model.VIDEOLISTMODEL
import com.tongge.ui.m1_video.model.VideoListModel

/**
 * 播放界面
 */
class PlayerActivity : BaseToorbarActivity(), View.OnClickListener, VideoPlayer.SurfaceStatusListener {

    private var myBind: ActivityPlayerBinding? = null
    private var player: VideoPlayer? = null
    private var screenBtn: ImageView? = null
    private var deviceInfo: VideoListModel? = null
    private var portraitLP: ConstraintLayout.LayoutParams? = null
    private var toolbarAnim: ValueAnimator? = null
    private var loadingBar: ProgressBar? = null
    private var isFristerIn: Boolean = true
    private var callBack: PlayerStatusListener? = null


    private val handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                0 -> {

//                    时差超过3秒则重置播放
                    if((System.currentTimeMillis()-SpUtil.getLastTime()>3000))
                    {
                        if(loadingBar?.visibility!=View.GONE)
                            loadingBar?.visibility = View.GONE
                        player?.play();
                    }

                    this.removeMessages(0)
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑

                    // app的功能逻辑处理
                    // 再次发出msg，循环更新
                    this.sendEmptyMessageDelayed(0, 1000)
                }

                1 ->
                    // 直接移除，定时器停止
                    this.removeMessages(0)

                else -> {
                }
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_player
    }

    override fun initLayout(dataBinding: ViewDataBinding) {
        myBind = dataBinding as ActivityPlayerBinding
        player = myBind?.playView
        player?.setListener(this)
        loadingBar = myBind?.playerPb
        screenBtn = myBind?.fullScreenBtn as ImageView
        deviceInfo = intent.getSerializableExtra(VIDEOLISTMODEL) as VideoListModel?
        val title: String? = String().let {
            val content: String? = if (!TextUtils.isEmpty(deviceInfo?.name?.get()))
                                        deviceInfo?.name?.get()
                                    else deviceInfo?.deviceID?.get()
            content
        }
        setTitleStr(title)
        myBind?.playerPb?.visibility = View.VISIBLE

        val playBtn = myBind?.play as Button
        playBtn.setOnClickListener(this)

        initEvents()
//        handler?.sendEmptyMessage(0);
    }

    private fun initEvents() {
        callBack = PlayerStatusListener()
        player?.setOnClickListener(this)
        screenBtn?.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        STCheckPermissions(this, Manifest.permission.RECORD_AUDIO)
    }

    override fun onResume() {
        super.onResume()
        player?.onResume()
        if (!isFristerIn)
            player?.prepare(deviceInfo?.deviceID?.get(), callBack)
        hiddenToolBar(false)
    }

    override fun onPause() {
        super.onPause()
        isFristerIn = false
        handler?.sendEmptyMessage(1);
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.destroy()
        player = null
    }

    private fun hiddenToolBar(isClick: Boolean) {
        val toolbar = myBind?.playToolbar
        if (isClick && toolbar?.visibility == View.VISIBLE) {
            toolbar.visibility = View.GONE
            return
        }
        toolbar?.visibility = View.VISIBLE
        if (toolbarAnim == null) {
            toolbarAnim = ValueAnimator.ofInt(60, 0).apply {
                duration = 1000 * 5
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        toolbar?.visibility = View.GONE
                    }
                })
            }
        }
        toolbarAnim?.start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
        //播放界面点击，显示/隐藏工具栏
            R.id.play_view -> hiddenToolBar(true)
        //全屏/半屏切换
            R.id.full_screen_btn -> changeScreenScape()
        //播放
            R.id.play -> {
//                player?.play()
                loadingBar?.visibility = View.GONE
                handler?.sendEmptyMessage(0);
            }
        }
    }



    /********************************************
     *              半屏/全屏切换                 *
     ********************************************/
    private fun changeScreenScape() {
        when (resources.configuration.orientation) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_USER -> {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
                portraitLP = player?.layoutParams as ConstraintLayout.LayoutParams
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        when (newConfig?.orientation) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_USER -> {
                supportActionBar?.hide()
                player?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                val dm = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(dm)
                player?.layoutParams = ConstraintLayout.LayoutParams(dm.widthPixels, dm.heightPixels)
                screenBtn?.setImageResource(R.mipmap.half_screen)
            }
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
                player?.layoutParams = portraitLP
                supportActionBar?.show()
                player?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                screenBtn?.setImageResource(R.mipmap.full_sceeen)
            }
        }
        hiddenToolBar(false)
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            super.onBackPressed()
        }
    }

    /********************************************
     *              请求视频回调                  *
     ********************************************/
    private inner class PlayerStatusListener : NetCoreBaseCallBack() {
        override fun ppcs_callback(handle: Int, status: Int) {
            STLog.d(this, "PlayerStatus " + status)
            when (status) {
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_CONNECTING -> {
                    STLog.d(this, "PSTA_CONNECTING")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_CONNECT_FAIL -> {
                    STLog.d(this, "PSTA_CONNECT_FAIL")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_CONNECT_SUCCESS -> {
                    player?.play()
                    STLog.d(this, "PSTA_CONNECT_SUCCESS")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_CONNEC_DISCONNECT -> {
                    STLog.d(this, "PSTA_CONNEC_DISCONNECT")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_CONNEC_ERROR -> {
                    STLog.d(this, "PSTA_CONNEC_ERROR")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_MEDIA_STREAM -> {
                    STLog.d(this, "PSTA_MEDIA_STREAM")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_MEDIA_HIST_STREAM -> {
                    STLog.d(this, "PSTA_MEDIA_HIST_STREAM")
                }
                NetCoreResultParams.PPCS_STATUS_CODE.PSTA_HIST_LIST -> {
                    STLog.d(this, "PSTA_HIST_LIST")
                }
            }
        }
    }

    /********************************************
     *              视频渲染回调                  *
     ********************************************/
    override fun onSurfaceCreated() {
        player?.prepare(deviceInfo?.deviceID?.get(), callBack)
    }

    override fun onSurfaceChanged() {
    }

    override fun onDrawFrame() {
    }

}
