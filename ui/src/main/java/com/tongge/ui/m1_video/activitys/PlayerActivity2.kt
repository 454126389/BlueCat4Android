package com.tongge.ui.m1_video.activitys

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.databinding.ViewDataBinding
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
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
class  PlayerActivity2 : BaseToorbarActivity(), View.OnClickListener, VideoPlayer.SurfaceStatusListener {

    private var player: VideoPlayer? = null
    private var myBind: ActivityPlayerBinding? = null
    private var callBack: PlayerActivity2.PlayerStatusListener? = null

    override fun getContentView(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return R.layout.activity_player
    }



    override fun initLayout(dataBinding: ViewDataBinding) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        callBack = PlayerStatusListener()
        myBind = dataBinding as ActivityPlayerBinding
        player = myBind?.playView
        player?.setListener(this)
//        player?.play()

    }


    override fun onSurfaceCreated() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        player?.prepare("PPCS-014950-LFMTD", callBack)
    }

    override fun onSurfaceChanged() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDrawFrame() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun onClick(v: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        when (v?.id) {
        //播放
            R.id.play -> {
                player?.play()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        player?.onResume()
//        if (!isFristerIn)
            player?.prepare("PPCS-014950-LFMTD", callBack)
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

}
