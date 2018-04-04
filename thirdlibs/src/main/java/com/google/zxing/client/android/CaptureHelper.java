/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.client.android.camera.CameraManager;
import com.tongge.thirdlibs.R;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * 去掉其他乱七八糟的东西，只保留扫码功能，把activity改成工具类
 * 在activity中使用： onCreate中调用 initCamera(Activity activity)
 *                  onResume中调用 resumeCamera(ViewfinderView viewfinderView, SurfaceView surfaceView)
 *                  onPause中调用 pauseCamera(SurfaceView surfaceView)
 *                  onDestory中调用 destroyCamera()
 * 然后实现QRScanProtocol接口
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */

public final class CaptureHelper implements SurfaceHolder.Callback {

    private static final String TAG = CaptureHelper.class.getSimpleName();

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(ResultMetadataType
            .ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE, ResultMetadataType.ERROR_CORRECTION_LEVEL,
            ResultMetadataType.POSSIBLE_COUNTRY);

    public Activity activity;
    private QRScanProtocol delegate;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    public void initCamera(Activity activity, QRScanProtocol delegate) {
        this.activity = activity;
        this.delegate = delegate;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this.activity);
        beepManager = new BeepManager(this.activity);
        ambientLightManager = new AmbientLightManager(this.activity);
    }

    public void resumeCamera(ViewfinderView viewfinderView, SurfaceView surfaceView) {

        cameraManager = new CameraManager(activity.getApplication());

        this.viewfinderView = viewfinderView;
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        if (prefs.getBoolean(PreferencesActivity.KEY_DISABLE_AUTO_ORIENTATION, true)) {
            activity.setRequestedOrientation(getCurrentOrientation());
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        inactivityTimer.onResume();

        decodeFormats = null;
        characterSet = null;
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }

    private int getCurrentOrientation() {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_270:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
    }

    public void pauseCamera(SurfaceView surfaceView) {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    public void destroyCamera() {
        inactivityTimer.shutdown();
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        if (delegate != null) {
            delegate.scanResult(rawResult, barcode, scaleFactor);
            inactivityTimer.onActivity();
            beepManager.playBeepSoundAndVibrate();
        }
        continuePreview();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
        }
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void continuePreview() {
        if (handler != null) {
            Message message = Message.obtain(handler, R.id.restart_preview, savedResultToShow);
            handler.sendMessage(message);
        }
    }
}
