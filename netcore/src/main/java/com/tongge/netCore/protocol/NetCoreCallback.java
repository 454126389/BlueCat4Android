package com.tongge.netCore.protocol;

import com.tongge.netCore.cameraInfo.CameraInfo;

/**
 * Created by DZ on 2017/6/16.
 */
public interface NetCoreCallback {
    void ppcs_callback(int handle, int status);
    void callback_servers(int status);
    void callback_register(int err_code);
    void callback_binddcam(int err_code);
    void callback_unbinddcam(int err_code);
    void callback_camlist(int count, CameraInfo cameraInfo[]);
    void callback_stream_stream(int camerid);
}
