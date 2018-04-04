package com.tongge.ui.m1_video.activitys

import android.app.Activity
import android.os.Handler

/**
 * Created by Administrator on 2017/11/28/028.
 */

class test : Activity() {
    private val handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                0 -> {
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    this.removeMessages(0)
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
}
