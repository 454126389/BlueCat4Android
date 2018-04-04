package com.tongge.ui.m1_video.model

import android.app.Activity
import android.view.View
import com.tongge.eventBus.BindMessageEvents
import com.tongge.eventBus.EventBusUtils
import com.tongge.manager.user.BCUserActionManager
import com.tongge.netCore.protocol.NetCoreBaseCallBack
import com.tongge.netCore.protocol.NetCoreResultParams
import com.tongge.ui.R
import com.tongge.ui.base.DefaultItemEvent
import com.tongge.ui.base.DefaultItemMoedl
import com.tongge.ui.utils.shortToast

/**
 * Created by DZ on 2017/7/5.
 *
 */
class DeviceInfoEvent : DefaultItemEvent{

    override fun defaultItemClick(view: View, model: DefaultItemMoedl) {
        if (model.itemFlags.get() == -1){ //解绑设备
            val activity = view.context as Activity
            BCUserActionManager.getInstace().
                    deleteDevice(model.rightStr.get(),
                            object : NetCoreBaseCallBack(){
                                override fun callback_binddcam(err_code: Int) {
                                    if (err_code == NetCoreResultParams.BindDevices.BCS_BD_ERR_SUCCESS){
                                        EventBusUtils.post(BindMessageEvents(err_code, view.context.getString(R.string.unbind)))
                                        activity.finish()
                                    }else
                                        shortToast(view.context,view.context.getString(R.string.unbind_failed))
                                }
                            })
        }else
            shortToast(view.context, model.leftStr.get())
    }

}