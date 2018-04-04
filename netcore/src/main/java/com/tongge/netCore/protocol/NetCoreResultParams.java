package com.tongge.netCore.protocol;

/**
 * Created by DZ on 2017/6/30.
 */

public interface NetCoreResultParams {
    
    interface BCS_STATUS_CODE{

        int BSTA_LOGIN_SUCCESS      = 0x0;          //bcs_login_success_event
        int BSTA_LOGIN_FAILED       = 0x1;          //bcs_login_failed_event
        int BSTA_MNG_SUCCESS        = 0x2;          //manage server success
        int BSTA_MNG_FAILED         = 0x3;          //manage server failed
        int BSTA_INFO_SUCCESS       = 0x4;          //information server success
        int BSTA_INFO_FAILED        = 0x5;          //information server failed
        int BSTA_CAMERA_LIST        = 0x6;          //bcs_camera_list
        int BSTA_MEDIA_STREAM       = 0x7;          //bcs_media_stream_event
        int BSTA_MEDIA_HIST_STREAM  = 0x8;          //bcs_media_stream_event
        int BSTA_HIST_LIST          = 0x9;          //bcs_hist_list_event
        int BSTA_REGISTER           = 0xa;          //bcs_register_event
        int BSTA_BIND_CAMERA        = 0xb;          //bcs_bind_camera_event
        int BSTA_UNBIND_CAMERA      = 0xc;          //bcs_unbind_camera_event
    }

    /**
     * 绑定或解绑
     */
    interface BindDevices{
        int BDS_BD_ERR_UNKNOWN      = -4;           //未知原因
        int BDS_BD_ERR_HAS_BOUND    = -1;           //失败
        int BCS_BD_ERR_SUCCESS      = 0;            //绑定／解绑成功
    }

    interface BCS_FRAME_TYPE{
        int BCS_VIDEO_H264_START    = 0;            //for H.264 Type
        int BCS_VIDEO_H264_IFRAME   = 1;
        int BCS_VIDEO_H264_PFRAME   = 2;
        int BCS_VIDEO_H264_BFRAME   = 3;
        int BCS_VIDEO_H264_BUTT     = 4;

        int BCS_AUDIO_START         = 100;
        int BCS_AUDIO_G711A         = 101;
        int BCS_AUDIO_PCM           = 102;
        int BCS_AUDIO_BUTT          = 103;
    }

    interface BCS_PTZ_DIRECTION{
        int BCS_PTZ_STOP            = 0 ;
        int BCS_PTZ_UP              = 1 ;
        int BCS_PTZ_DOWN            = 2 ;
        int BCS_PTZ_LEFT            = 3 ;
        int BCS_PTZ_RIGHT           = 4 ;
        int BCS_PTZ_CHECKSELF       = 5 ;
    }

    interface PPCS_STATUS_CODE{
        int PSTA_CONNECTING         = 0x0;
        int PSTA_CONNECT_FAIL       = 0x1;
        int PSTA_CONNECT_SUCCESS    = 0x2;
        int PSTA_CONNEC_DISCONNECT  = 0x3;
        int PSTA_CONNEC_ERROR       = 0x4;
        int PSTA_MEDIA_STREAM       = 0x5;//ppcs_media_stream_event
        int PSTA_MEDIA_HIST_STREAM  = 0x6;//ppcs_media_stream_event
        int PSTA_HIST_LIST          = 0x7;//ppcs_hist_list_event
    }

    interface BCS_STREAM_TYPE{
        int BCS_STREAM_TYPE_MAIN    = 0 ;
        int BCS_STREAM_TYPE_SUB     = 1 ;
    }

    interface BCS_STREAM_ON_OFF{
        int BCS_STREAM_OFF          = 0 ;
        int BCS_STREAM_ON           = 1 ;
    }

    interface BCS_MEDIA_TYPE{
        int BCS_MEDIA_TYPE_VIDEO    = 0 ;
        int BCS_MEDIA_TYPE_AUDIO    = 1;
    }

    interface bcs_register_errcode{
        int BCS_REG_ERR_SUCCESS     = 0x0 ;
        int BCS_REG_ERR_UNKNOWN     = 0x1 ;
        int BCS_REG_ERR_INVALID_TOKEN = 0x2 ;
        int BCS_REG_ERR_NOT_SUPPORT = 0x3 ;
        int BCS_REG_ERR_REPEAT      = 0x4 ;
    }

    interface bcs_client_type{
        int BCS_CLIENT_PC           = 0x0 ;
        int BCS_CLIENT_ANDROID      = 0x1 ;
        int BCS_CLIENT_IOS          = 0x2 ;
    }
    
}
