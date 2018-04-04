package com.tongge.ui.m1_video.activitys

import android.Manifest
import android.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.view.SurfaceView
import com.google.zxing.Result
import com.google.zxing.client.android.CaptureHelper
import com.google.zxing.client.android.QRScanProtocol
import com.google.zxing.client.android.ViewfinderView
import com.tongge.common.mathUtils.RegexUtils
import com.tongge.common.sysUtils.STCheckPermissions
import com.tongge.common.sysUtils.STLog
import com.tongge.eventBus.BindMessageEvents
import com.tongge.eventBus.EventBusUtils
import com.tongge.manager.user.BCUserActionManager
import com.tongge.netCore.protocol.NetCoreBaseCallBack
import com.tongge.ui.R
import com.tongge.ui.base.BaseToorbarActivity
import com.tongge.ui.databinding.ActivityQrscanBinding
import com.tongge.ui.utils.shortToast

class QRScanActivity : BaseToorbarActivity(), QRScanProtocol {

    private var scanHelper: CaptureHelper? = null
    private var surfaceView: SurfaceView? = null
    private var maskView: ViewfinderView? = null

    override fun getContentView(): Int {
        return R.layout.activity_qrscan
    }

    override fun initLayout(dataBinding: ViewDataBinding) {
        setTitleStr(R.string.scan_title)
        scanHelper = CaptureHelper()
        scanHelper?.initCamera(this, this)
        val myBind = dataBinding as ActivityQrscanBinding
        surfaceView = myBind.previewView
        maskView = myBind.viewfinderView
    }

    override fun scanResult(rawResult: Result?, barcode: Bitmap?, scaleFactor: Float) {
        STLog.d(TAG,rawResult?.text+"")
        if (RegexUtils.checkDevicesSN(rawResult?.text)){
            BCUserActionManager.getInstace().
                    addDevice(rawResult?.text,
                            object : NetCoreBaseCallBack(){
                                override fun callback_binddcam(err_code: Int) {
                                    EventBusUtils.post(BindMessageEvents(err_code, getString(R.string.bind)))
                                    finish()
                                }
                            })
        }else{
            shortToast(this, getString(R.string.unlawfulness_sn))
        }
    }

    override fun onStart() {
        super.onStart()
        STCheckPermissions(this, Manifest.permission.CAMERA)
    }

    override fun onResume() {
        super.onResume()
        scanHelper?.resumeCamera(maskView, surfaceView)
    }

    override fun onPause() {
        super.onPause()
        scanHelper?.pauseCamera(surfaceView)
    }

    override fun onDestroy() {
        super.onDestroy()
        scanHelper?.destroyCamera()
    }

}
