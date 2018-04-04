package com.tongge.ui.utils

/**
 * Created by DZ on 2017/5/23.
 *
 */
class AppCommonParamsUtil private constructor(){
    var logoIcon: Int = 0                       //app logo图标res ID
    var loginIcon: Int = 0                      //app登陆界面图标res ID
    var isSuccWithNetCore: Boolean = false      //netCore是否初始化成功

    companion object{
        fun shareInstance(): AppCommonParamsUtil {
            return Signle.single
        }
    }

    private object Signle{
        val single = AppCommonParamsUtil()
    }

}