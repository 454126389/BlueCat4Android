package com.tongge.ui.base

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.tongge.common.netUtils.NetworkBroadcastReceiver
import com.tongge.common.netUtils.NetworkUtils
import com.tongge.common.sysUtils.AppManger
import com.tongge.common.sysUtils.STRequestPermissionsResult
import com.tongge.ui.R
import com.tongge.ui.utils.shortToast

open class BaseActivity : AppCompatActivity(), NetworkBroadcastReceiver.NetWorkSListener {

    val TAG: String by lazy {
        this.javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManger.getInstance().addActivity(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home){
            AppManger.getInstance().finishActivity(this)
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManger.getInstance().finishActivity(this)
    }

    /**
     * API 23后的权限申请回调
     */
    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        STRequestPermissionsResult(this, requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 网络状态监听
     */
    override fun networkDisConnected() {
        shortToast(this,getString(R.string.network_not_use))
    }

    override fun networkChanged(type: NetworkUtils.NetworkType) {
    }
}
