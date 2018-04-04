package com.google.zxing.client.android;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * 二维码扫描结果
 * Created by DZ on 2017/6/1.
 */

public interface QRScanProtocol {
    void scanResult(Result rawResult, Bitmap barcode, float scaleFactor);
}
