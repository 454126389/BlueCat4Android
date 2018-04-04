package com.tongge.netCore.cameraInfo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tongge.common.spUtils.SpUtil;
import com.tongge.common.sysUtils.STLog;
import com.tongge.netCore.NetCore;
import com.tongge.netCore.protocol.NetCoreCallback;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.tongge.netCore.protocol.NetCoreResultParams.BCS_STREAM_ON_OFF.BCS_STREAM_OFF;
import static com.tongge.netCore.protocol.NetCoreResultParams.BCS_STREAM_ON_OFF.BCS_STREAM_ON;
import static com.tongge.netCore.protocol.NetCoreResultParams.BCS_STREAM_TYPE.BCS_STREAM_TYPE_MAIN;

/**
 * Created by Marvin on 2017/4/11.
 */
public class VideoPlayer extends GLSurfaceView implements GLSurfaceView.Renderer {
    private NetCore m_netCore;
    private long videoRenderHandler = 0;
    private int dev_id = 10000004;
    private SurfaceStatusListener listener;

    public VideoPlayer(Context context) {
        super(context);
        init();
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void OnDrawFrame(long handler) {
        this.requestRender();
    }

    private void init(){
        m_netCore = NetCore.getInstance();
        setEGLContextClientVersion(2); // 设置使用OPENGL ES2.0
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(this); // 设置渲染器
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        m_netCore.setVideoCallback(this);
    }

    public void setListener(SurfaceStatusListener listener) {
        this.listener = listener;
    }

    /**
     * 准备播放器
     * 必须在onCreate之后调用
     * @param callback  状态回调
     */
    public void prepare(String deviceID, NetCoreCallback callback) {
        m_netCore.setCallback(callback);
        if (m_netCore != null) {
//            if (videoRenderHandler <= 0)
//                videoRenderHandler = m_netCore.ppcs_player_init();
            m_netCore.PPCS_connect(deviceID);
            m_netCore.ppcs_player_bind(m_netCore.getPpcs_handle(), (int) videoRenderHandler);
        }
    }

    /**
     * 播放，必须在activity的onResume()之后调用
     * @return 是否成功
     */
    public boolean play(){
        if (!m_netCore.ppcs_req_video(m_netCore.getPpcs_handle(),
                BCS_STREAM_TYPE_MAIN,
                BCS_STREAM_ON)){
            STLog.e(this,"ppcs_req_video play failed");
            return false;
        }
        STLog.d(this,"ppcs_req_video play success");
        return true;
    }

    /**
     * 停止播放，必须在super.onResume()之后调用
     * @return 是否成功
     */
    public boolean stop(){
        if (!m_netCore.ppcs_req_video(m_netCore.getPpcs_handle(),
                BCS_STREAM_TYPE_MAIN,
                BCS_STREAM_OFF)){
            STLog.e(this,"ppcs_req_video stop failed");
            return false;
        }
        STLog.d(this,"ppcs_req_video stop success");
        if (!m_netCore.ppcs_disconnect(m_netCore.getPpcs_handle())){
            STLog.e(this,"ppcs_disconnect failed");
            return false;
        }
        STLog.d(this,"ppcs_disconnect success");
        videoRenderHandler = 0;
        return true;
    }

    /**
     * 断开连接
     * @return
     */
    public boolean destroy(){
        if (!m_netCore.ppcs_req_video(m_netCore.getPpcs_handle(),
                BCS_STREAM_TYPE_MAIN,
                BCS_STREAM_OFF)){
            STLog.e(this,"ppcs_req_video stop failed");
            return false;
        }
        STLog.d(this,"ppcs_req_video stop success");
        if (!m_netCore.ppcs_disconnect(m_netCore.getPpcs_handle())){
            STLog.e(this,"ppcs_disconnect failed");
            return false;
        }
        videoRenderHandler = 0;
        STLog.d(this,"ppcs_disconnect success");
        return true;
    }

    /**********************************************
     *                                            *
     *                  ⬇️渲染器⬇️                  *
     *                                            *
     **********************************************/

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        if (m_netCore != null) {
            //videoRenderHandler = m_netCore.sdk_player_init(dev_id);
            videoRenderHandler = m_netCore.ppcs_player_init();
            if (listener != null && videoRenderHandler > 0)
                listener.onSurfaceCreated();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (m_netCore != null) {
            //m_netCore.sdk_player_resize(dev_id, width, height);
            m_netCore.ppcs_player_resize((int) videoRenderHandler, width, height);
            if (listener != null )
                listener.onSurfaceChanged();
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        SpUtil.setLastTime(System.currentTimeMillis());

        if (m_netCore != null) {
            //m_netCore.sdk_player_drawframe(dev_id);
            if(m_netCore.ppcs_player_drawframe((int) videoRenderHandler)){
                Log.d("shit","onDrawFrame---success");
            }else{
                Log.d("shit","onDrawFrame---falied");
            }
            if (listener != null )
                listener.onDrawFrame();
        }else
        {
            Log.d("shit","onDrawFrame---null");
        }


    }

    /**
     * 播放器活动状态监听
     */
    public interface SurfaceStatusListener{
        /**
         * 播放器建立成功
         */
        void onSurfaceCreated();

        /**
         * 播放器改变
         */
        void onSurfaceChanged();

        /**
         * 播放器渲染画面时调用，尽量不要在这搞费时的事情
         */
        void onDrawFrame();
    }
}
