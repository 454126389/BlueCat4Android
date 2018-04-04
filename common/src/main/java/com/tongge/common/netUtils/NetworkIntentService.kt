package com.tongge.common.netUtils

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

/**
 *  IntentService,网络方面的事尽量在这里干
 *
 */
class NetworkIntentService : IntentService("NetworkIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            when (action) {
                ACTION_NETWORK_LINSENER -> startNetworkBroadcastReceiver()
            }
        }
    }

    /**
     *  动态注册监听网络状态广播，静态注册在API 21提示过期，国内也用不了GCMNetworkManager
     **/
    private fun startNetworkBroadcastReceiver() {
        val netBraoadcast = NetworkBroadcastReceiver()
        val mFilter = IntentFilter()
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netBraoadcast, mFilter)
//        unregisterReceiver(netBraoadcast)
    }

    companion object {
        private val ACTION_NETWORK_LINSENER = "ACTION_NETWORK_LINSENER"

        fun startActionForNetworkLinsener(context: Context) {
            val intent = Intent(context, NetworkIntentService::class.java)
            intent.action = ACTION_NETWORK_LINSENER
            context.startService(intent)
        }
    }
}
