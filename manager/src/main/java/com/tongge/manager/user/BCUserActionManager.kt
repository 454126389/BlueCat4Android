package com.tongge.manager.user

import com.tongge.common.otherUtils.ThreadPoolUtils
import com.tongge.common.sysUtils.STLog
import com.tongge.netCore.NetCore
import com.tongge.netCore.protocol.NetCoreCallback

/**
 *
 * 用户操作请求管理
 * Created by DZ on 2017/6/30.
 *
 */
class BCUserActionManager private constructor(){
    private val TAG = this::class.java.simpleName
    private val netCore = NetCore.getInstance()
    companion object{
        fun getInstace(): BCUserActionManager{
            return Single.singleton
        }
    }

    private object Single{
        val singleton = BCUserActionManager()
    }

    /**
     * 请求视频列表
     */
    fun requestDevicesList(callback: NetCoreCallback){
        netCore.setCallback(callback)
        ThreadPoolUtils.getInstace().setExecs_key(ThreadPoolUtils.FixedThread).execute({
            if (!netCore.sdk_req_camlist())
                STLog.e(TAG, "sdk_req_camlist failed ")
            else
                STLog.d(TAG, "sdk_req_camlist success ")
        })
    }

    /**
     * 添加设备
     */
    fun addDevice(deviceID: String?, callback: NetCoreCallback){
        netCore.setCallback(callback)
        ThreadPoolUtils.getInstace().setExecs_key(ThreadPoolUtils.FixedThread).execute({
            if (!netCore.sdk_bind_camera(deviceID))
                STLog.e(TAG, "sdk_bind_camera failed ")
            else
                STLog.d(TAG, "sdk_bind_camera success ")
        })
    }

    /**
     * 删除设备
     */
    fun deleteDevice(deviceID: String?, callback: NetCoreCallback){
        netCore.setCallback(callback)
        ThreadPoolUtils.getInstace().setExecs_key(ThreadPoolUtils.FixedThread).execute({
            if (!netCore.sdk_unbind_camera(deviceID))
                STLog.e(TAG, "sdk_unbind_camera failed ")
            else
                STLog.d(TAG, "sdk_unbind_camera success ")
        })
    }
}