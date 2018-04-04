package com.tongge.common.dataUtils

import android.content.Context
import android.os.Environment
import com.tongge.common.sysUtils.STLog
import java.io.File

/**
 * Created by DZ on 2017/7/11.
 *
 */

     fun getAppDataFile(context: Context): String {
        val path: String
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            STLog.e("getAppDataFile", "SD卡不可用")
            path = context.externalCacheDir.absolutePath
        } else
            path = Environment.getExternalStorageDirectory().absolutePath +
                    File.separator + context.packageName.split('.').last()
        val file = File(path)
        if (!file.exists())
            file.mkdir()
        return file.absolutePath + File.separator
    }
