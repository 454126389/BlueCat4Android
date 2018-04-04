package com.tongge.manager.user

import com.tongge.common.sysUtils.STLog
import com.tongge.netCore.NetCore
import com.tongge.netCore.protocol.NetCoreCallback

/**
 * 登陆／注册／找回密码等操作
 * Created by DZ on 2017/5/24.
 *
 */
class BCUserManager private constructor(){
    private val TAG = this::class.java.simpleName
    var user: IUser? = null
    private val netCore = NetCore.getInstance()

    companion object{
        fun shareInstance(): BCUserManager {
            return Signle.single
        }
    }

    private object Signle{
        val single = BCUserManager()
    }

    fun login(callback: NetCoreCallback){
        netCore.setCallback(callback)
        if (!netCore.sdk_login(user?.getCUsername(), user?.getCPassword())){
            STLog.e(TAG, "sdk_login fail ")

            return
        }
    }

    fun register(rgn: String, phone: String, code: String, pwd: String){
        if (!netCore.sdk_register_add_user(rgn, phone, code, pwd)){
            STLog.e(TAG, "sdk_register_add_user fail ")
            return
        }
    }

    fun registerCode(rgn: String, phone: String){
        if (!netCore.sdk_register_req_phone_code(rgn, phone)){
            STLog.e(TAG, "sdk_register_req_phone_code fail ")
            return
        }
    }

}