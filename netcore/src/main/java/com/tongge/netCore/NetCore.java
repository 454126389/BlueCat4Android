package com.tongge.netCore;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.tongge.common.sysUtils.STLog;
import com.tongge.netCore.cameraInfo.CameraInfo;
import com.tongge.netCore.cameraInfo.VideoPlayer;
import com.tongge.netCore.protocol.NetCoreCallback;
import com.tongge.netCore.protocol.NetCoreResultParams;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class NetCore {

    /*********************** callback标识 ***************************/
    private static final int CALLBACK_PPCS = 0x1;
    private static final int CALLBACK_SERVERS = 0x2;
    private static final int CALLBACK_REGISTER = 0x3;
    private static final int CALLBACK_BINDDCAM = 0x4;
    private static final int CALLBACK_UNBINDDCAM = 0x5;
    private static final int CALLBACK_CAMLIST = 0x6;
    private static final int CALLBACK_STREAM_STREAM = 0x7;

    /**
     * <p>连接服务器标识，默认 0x1111</p>
     * <p>当三次服务器连接都成功时应为 0x1000</p>
     */
    public static int BC_SRV_STATUS = 0x1111;
    private final String TAG = this.getClass().getSimpleName();

    private CameraInfo cameraInfo;

    private VideoPlayer m_videoSurface;
    //    private NetCoreCallback callback;   //外面传进来的回调
    private NetCoreCallBackHandler handler;
    private int ppcs_handle = 0;
    private boolean isConnectInfoServerSucc = true;

    private NetCore() {
        {
            System.loadLibrary("bc_netcore");
        }
        if (!setJNIEnv()) {
            STLog.e(TAG, "start setJNIEnv failed ");
            return;
        }
        cameraInfo = new CameraInfo();
        handler = new NetCoreCallBackHandler(Looper.getMainLooper());
        STLog.d(TAG, "load native success ");
    }

    public static final NetCore getInstance() {
        return SingletonHoler.INSTANCE;
    }

    private static final class SingletonHoler {
        private static final NetCore INSTANCE = new NetCore();
    }

    public boolean netCore_init() {
        if (!sdk_init()) {
            STLog.e(TAG, "sdk_init failed ");
            return false;
        }
        cameraInfo.SetClassRef();
        STLog.d(TAG, "netCore_init success ");
        return true;
    }

    public boolean PPCS_init(String key) {
        if (TextUtils.isEmpty(key)) {
            key = "EBGAEIBIKHJJGFJKEOGCFAEPHPMAHONDGJFPBKCPAJJMLFKBDBAGCJPBGOLKIKLKAJMJKFDOOFMOBECEJIMM";
        }
        if (!ppcs_init(key)) {
            STLog.e(TAG, "ppcs_init failed ");
            return false;
        }
        STLog.d(TAG, "ppcs_init success ");

        return true;
    }

    public boolean PPCS_connect(String device_id) {
        ppcs_handle = ppcs_start("ppcs_callback");
        Log.d("test","---"+ppcs_handle);
        if (ppcs_handle == 0) {
            STLog.e(TAG, "ppcs_start failed ");
            return false;
        }
        STLog.d(TAG, "ppcs_start success ");
        if (!ppcs_connect(ppcs_handle, device_id)) {
            STLog.e(TAG, "ppcs_connect failed ");
            return false;
        }
        STLog.d(TAG, "ppcs_connect success ");
        return true;
    }

    public int getPpcs_handle() {
        return ppcs_handle;
    }

    public boolean deinit() {
        sdk_deinit();
        if (!ppcs_deinit()) {
            STLog.e(TAG, "ppcs_deinit failed");
            return false;
        }
        handler.removeCallbacksAndMessages(null);
        STLog.d(TAG, "netCore_deinit success");
        return true;
    }

    private native boolean setJNIEnv();

    /****************** BC Platform SDK ******************/
    private native boolean sdk_init();

    public native boolean sdk_login(String username, String password);

    public native boolean sdk_req_camlist();

    public native void sdk_req_play_camera(int CameraID);

    public native void sdk_stop_play_camera(int CameraID);

    private native void sdk_deinit();

    public native long sdk_player_init(int CameraID);

    public native void sdk_player_deinit(int CameraID);

    public native boolean sdk_player_drawframe(int CameraID);

    public native boolean sdk_player_resize(int CameraID, int width, int height);

    public native boolean sdk_register_req_phone_code(String rgn, String phone);

    public native boolean sdk_register_add_user(String rgn, String phone, String code, String pwd);

    public native boolean sdk_bind_camera(String device_id);

    public native boolean sdk_unbind_camera(String device_id);

    /**
     * PPCS
     **/
    public native boolean ppcs_init(String server);

    private native boolean ppcs_deinit();

    public native int ppcs_start(String method);

    public native void ppcs_stop(int handle);

    public native boolean ppcs_connect(int handle, String did);

    public native boolean ppcs_disconnect(int handle);

    public native boolean ppcs_req_video(int handle, int stream_type, int onoff);

    public native int ppcs_player_init();

    public native boolean ppcs_player_deinit(int handle);

    public native boolean ppcs_player_drawframe(int handle);

    public native boolean ppcs_player_resize(int handle, int w, int h);

    public native boolean ppcs_player_bind(int ppcs_handle, int player_handle);

    public native boolean sdk_set_push_info(int onoff, String token);

    //public void ppcs_callback (int handle, String did, int status, Objects obj)
    public void ppcs_callback(int handle, int status) {
        STLog.d(TAG, "ppcs_callback " + status);
        if (status == 5) {
            if (m_videoSurface != null) m_videoSurface.OnDrawFrame(handle);
        }
        Message message = new Message();
        message.what = CALLBACK_PPCS;
        message.arg1 = handle;
        message.arg2 = status;
        handler.sendMessage(message);
    }

    /**
     * <p>连接服务器回调</p>
     *
     * @param status 有3中可能<Br/>
     *               <div>1、登陆<Br/>&nbsp&nbsp&nbsp&nbsp
     *               {@link NetCoreResultParams.BCS_STATUS_CODE.BSTA_LOGIN_SUCCESS}
     *               <Br/>&nbsp&nbsp&nbsp&nbsp
     *               {@link NetCoreResultParams.BCS_STATUS_CODE.BSTA_LOGIN_FAILED}
     *               </div>
     *               <div>
     *               2、连接管理服务器<Br/>&nbsp&nbsp&nbsp&nbsp
     *               {@link NetCoreResultParams.BCS_STATUS_CODE.BSTA_MNG_SUCCESS}
     *               <Br/>&nbsp&nbsp&nbsp&nbsp
     *               {@link NetCoreResultParams.BCS_STATUS_CODE.BSTA_MNG_FAILED}
     *               </div>
     *               <div>
     *               3、设备管理器<Br/>&nbsp&nbsp&nbsp&nbsp
     *               {@link NetCoreResultParams.BCS_STATUS_CODE.BSTA_INFO_SUCCESS}
     *               <Br/>&nbsp&nbsp&nbsp&nbsp
     *               {@link NetCoreResultParams.BCS_STATUS_CODE.BSTA_INFO_FAILED}
     *               </div>
     */
    public void callback_servers(int status) {
        switch (status) {
            case NetCoreResultParams.BCS_STATUS_CODE.BSTA_LOGIN_SUCCESS:
                BC_SRV_STATUS &= 0x1110;
                STLog.d(TAG, "BSTA_LOGIN_SUCCESS " + status);
                break;
            case NetCoreResultParams.BCS_STATUS_CODE.BSTA_MNG_SUCCESS:
                BC_SRV_STATUS &= 0x1101;
                STLog.d(TAG, "BSTA_MNG_SUCCESS " + status);
                Message message = new Message();
                message.what = CALLBACK_SERVERS;
                message.arg1 = isConnectInfoServerSucc? status : NetCoreResultParams.BCS_STATUS_CODE.BSTA_INFO_FAILED;
                handler.sendMessage(message);
                break;
            case NetCoreResultParams.BCS_STATUS_CODE.BSTA_LOGIN_FAILED:
                STLog.d(TAG, "BSTA_LOGIN_FAILED " + status);
            case NetCoreResultParams.BCS_STATUS_CODE.BSTA_MNG_FAILED:
                Message message1 = new Message();
                message1.what = CALLBACK_SERVERS;
                message1.arg1 = status;
                handler.sendMessage(message1);
                break;
            case NetCoreResultParams.BCS_STATUS_CODE.BSTA_INFO_SUCCESS:
                BC_SRV_STATUS &= 0x1011;
                STLog.d(TAG, "BSTA_INFO_SUCCESS " + status);
                isConnectInfoServerSucc = true;
                break;
            case NetCoreResultParams.BCS_STATUS_CODE.BSTA_INFO_FAILED:
                STLog.d(TAG, "BSTA_INFO_FAILED " + status);
                isConnectInfoServerSucc = false;
                break;
        }
    }

    public void callback_register(int err_code) {
        STLog.d(TAG, "callback_register = " + err_code);
        Message message = new Message();
        message.what = CALLBACK_REGISTER;
        message.arg1 = err_code;
        handler.sendMessage(message);
    }

    public void callback_binddcam(int err_code) {
        STLog.d(TAG, "callback_binddcam err_code = " + err_code);
        Message message = new Message();
        message.what = CALLBACK_BINDDCAM;
        message.arg1 = err_code;
        handler.sendMessage(message);
    }

    public void callback_unbinddcam(int err_code) {
        STLog.d(TAG, "callback_unbinddcam err_code = " + err_code);
        Message message = new Message();
        message.what = CALLBACK_UNBINDDCAM;
        message.arg1 = err_code;
        handler.sendMessage(message);
    }

    public void callback_camlist(int count, CameraInfo cameraInfo[]) {
        STLog.d(TAG, "callback_camera_list count = " + count);
        Message message = new Message();
        message.what = CALLBACK_CAMLIST;
        message.arg1 = count;
        message.obj = cameraInfo;
        handler.sendMessage(message);
    }

    public void callback_stream_stream(int camerid) {
        STLog.d(TAG, "callback_video_stream camerid = " + camerid);
        if (m_videoSurface != null) m_videoSurface.OnDrawFrame(camerid);
        Message message = new Message();
        message.what = CALLBACK_STREAM_STREAM;
        message.arg1 = camerid;
        handler.sendMessage(message);
    }

    public void setVideoCallback(@NotNull VideoPlayer videoPlayer) {
        m_videoSurface = videoPlayer;
    }

    public void setCallback(@NotNull NetCoreCallback callback) {
        //        this.callback = callback;
        handler.setWeakCallback(callback);
    }

    /**
     * handler，切换到主线程搞事情
     */
    private static class NetCoreCallBackHandler extends Handler {
        private WeakReference<NetCoreCallback> weakCallback;

        NetCoreCallBackHandler(Looper looper) {
            super(looper);
        }

        private void setWeakCallback(@Nullable NetCoreCallback callback) {
            this.weakCallback = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakCallback == null) {
                STLog.e("NetCore Handler", "callBack is null, ID = " + msg.what);
                return;
            }
            STLog.d("NetCore Handler","statue = "+msg.arg1+"--"+msg.arg2+"--"+msg.what);
            switch (msg.what) {
                case CALLBACK_PPCS:
                    weakCallback.get().ppcs_callback(msg.arg1, msg.arg2);
                    break;
                case CALLBACK_SERVERS:
                    weakCallback.get().callback_servers(msg.arg1);
                    break;
                case CALLBACK_REGISTER:
                    weakCallback.get().callback_register(msg.arg1);
                    break;
                case CALLBACK_BINDDCAM:
                    weakCallback.get().callback_binddcam(msg.arg1);
                    break;
                case CALLBACK_UNBINDDCAM:
                    weakCallback.get().callback_unbinddcam(msg.arg1);
                    break;
                case CALLBACK_CAMLIST:
                    weakCallback.get().callback_camlist(msg.arg1, (CameraInfo[]) msg.obj);
                    break;
                case CALLBACK_STREAM_STREAM:
                    weakCallback.get().callback_stream_stream(msg.arg1);
                    break;

            }
        }
    }
}