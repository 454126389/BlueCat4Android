package com.tongge.common.netUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tongge.common.sysUtils.AppManger
import com.tongge.common.sysUtils.STLog

/**
 * 监听网络状态变化广播
 */
class NetworkBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val activity = AppManger.getInstance().currentActivity()

        if (NetworkUtils.isConnected() && NetworkUtils.isAvailableByPing()){
            //网络可用
            STLog.d(this, "当前网络正常")
            if (activity is NetWorkSListener){
                activity.networkChanged(NetworkUtils.getNetworkType())
            }
        }else{
            //网络不可用
            STLog.d(this, "当前网络不可用")
            if (activity is NetWorkSListener){
                activity.networkDisConnected()
            }
        }
    }

    interface NetWorkSListener{
        fun networkDisConnected()
        fun networkChanged(type: NetworkUtils.NetworkType)
    }
}
