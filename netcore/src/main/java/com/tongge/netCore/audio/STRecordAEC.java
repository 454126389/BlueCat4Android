package com.tongge.netCore.audio;

import android.media.audiofx.AcousticEchoCanceler;

import com.tongge.common.sysUtils.STLog;

/**
 * Created by DZ on 2017/7/13.
 */

public class STRecordAEC {

    private AcousticEchoCanceler aec;

    public STRecordAEC(int audioSession) {
        if (aec != null) {
            throw new UnsupportedOperationException(" AcousticEchoCanceler create error ");
        }
        aec = AcousticEchoCanceler.create(audioSession);
//        aec.setEnabled(true);
    }

    public void setEnabled(boolean enabled){
        if (aec == null){
            STLog.e(this, "aec is null");
            return;
        }
        aec.setEnabled(enabled);
        STLog.e(this, "aec setEnable success");
    }

    public void release() {
        if (null != aec) {
            aec.setEnabled(false);
            aec.release();
        }
    }


    /**
     * 是否支持ARC
     *
     * @return true,    false
     */
    public static boolean isSupportAEC() {
        return AcousticEchoCanceler.isAvailable();
    }

}
