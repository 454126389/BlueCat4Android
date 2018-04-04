package com.tongge.netCore.cameraInfo;

/**
 * Created by Marvin on 2017/4/7.
 */
public class CameraInfo {
    public String camera_name;
    public String device_id;
    public int  camera_id;
    public int  online;
    public int  owner;

    public native boolean SetClassRef ();

    @Override
    public String toString() {
        return "camera_name = "+camera_name+
                "\ndevice_id = "+device_id+
                "\ncamera_id = "+camera_id+
                "\nonline = "+online+
                "\nowner = "+owner;
    }
}
