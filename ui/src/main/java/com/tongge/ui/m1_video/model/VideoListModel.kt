package com.tongge.ui.m1_video.model

import android.content.Context
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.tongge.netCore.cameraInfo.CameraInfo
import com.tongge.ui.m1_video.activitys.DeviceInfoActivity
import com.tongge.ui.m1_video.activitys.PlayerActivity
import java.io.Serializable

const val VIDEOLISTMODEL = "VideoListModel"

/**
 * Created by DZ on 2017/5/26.
 *
 */
class VideoListModel() : Serializable {

    var name: ObservableField<String> = ObservableField()
    var deviceID: ObservableField<String> = ObservableField()
    var camearID: ObservableInt = ObservableInt()
    var owner: ObservableInt = ObservableInt()
    var isOnline: ObservableBoolean = ObservableBoolean(false)
    var imgUrl: ObservableField<String> = ObservableField()

    constructor(cameraInfo: CameraInfo) : this(){
        name.set(cameraInfo.camera_name)
        deviceID.set(cameraInfo.device_id)
        camearID.set(cameraInfo.camera_id)
        owner.set(cameraInfo.owner)
        isOnline.set(cameraInfo.online == 1)
    }

    fun itemClick(context: Context) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra(VIDEOLISTMODEL, this)
        context.startActivity(intent)
    }

    fun gotoDeviceSet(view: View) {
        val intent = Intent(view.context, DeviceInfoActivity::class.java)
        intent.putExtra(VIDEOLISTMODEL, this)
        view.context.startActivity(intent)
    }

}
