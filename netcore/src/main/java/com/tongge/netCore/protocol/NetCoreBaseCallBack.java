package com.tongge.netCore.protocol;

import com.tongge.netCore.cameraInfo.CameraInfo;

/**
 * Created by DZ on 2017/6/16.
 */
public abstract class NetCoreBaseCallBack implements NetCoreCallback {

    @Override
    public void ppcs_callback(int handle, int status) {

    }

    @Override
    public void callback_servers(int status) {

    }

    @Override
    public void callback_register(int err_code) {

    }

    @Override
    public void callback_binddcam(int err_code) {

    }

    @Override
    public void callback_unbinddcam(int err_code) {

    }

    @Override
    public void callback_camlist(int count, CameraInfo[] cameraInfo) {

    }

    @Override
    public void callback_stream_stream(int camerid) {

    }
}
