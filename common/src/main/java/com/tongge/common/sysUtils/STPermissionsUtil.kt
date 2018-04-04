package com.tongge.common.sysUtils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.tongge.common.R


/**
 * Created by DZ on 2017/7/10.
 *  API 23之后权限申请工具
 *
 *  联系人
 *       Manifest.permission.WRITE_CONTACTS
 *       Manifest.permission.GET_ACCOUNTS
 *       Manifest.permission.READ_CONTACTS
 *
 *  电话
 *       Manifest.permission.READ_CALL_LOG
 *       Manifest.permission.READ_PHONE_STATE
 *       Manifest.permission.CALL_PHONE
 *       Manifest.permission.WRITE_CALL_LOG
 *       Manifest.permission.USE_SIP
 *       Manifest.permission.PROCESS_OUTGOING_CALLS
 *       Manifest.permission.ADD_VOICEMAIL
 *
 *  日历
 *       Manifest.permission.READ_CALENDAR
 *       Manifest.permission.WRITE_CALENDAR
 *
 *  相机
 *       Manifest.permission.CAMERA
 *
 *  传感器
 *       Manifest.permission.BODY_SENSORS
 *
 *  定位
 *       Manifest.permission.ACCESS_FINE_LOCATION
 *       Manifest.permission.ACCESS_COARSE_LOCATION
 *
 *  存储
 *       Manifest.permission.READ_EXTERNAL_STORAGE
 *       Manifest.permission.WRITE_EXTERNAL_STORAGE

 *  录音
 *       Manifest.permission.RECORD_AUDIO

 *  短信
 *       Manifest.permission.READ_SMS
 *       Manifest.permission.RECEIVE_WAP_PUSH
 *       Manifest.permission.RECEIVE_MMS
 *       Manifest.permission.RECEIVE_SMS
 *       Manifest.permission.SEND_SMS
 */

const val PERMISSION_REQUEST_CODE = 0x1000

/**
 * 检查权限是否已请求到 (6.0)
 */
fun STCheckPermissions(activity: Activity, vararg permissions: String) {
    // 版本兼容
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            // 判断缺失哪些必要权限
            && lacksPermissions(activity, *permissions)) {
        // 如果缺失,则申请
        requestPermissions(activity, *permissions)
    }
}

/**
 * 判断是否缺失权限集合中的权限
 */
private fun lacksPermissions(activity: Activity, vararg permissions: String): Boolean {
    return permissions.any { lacksPermission(activity, it) }
}

/**
 * 判断是否缺少某个权限
 */
private fun lacksPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED
}

/**
 * 请求权限
 */
private fun requestPermissions(activity: Activity, vararg permissions: String) {
    ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)
}

/**
 * 启动应用的设置,进入手动配置权限页面
 */
private fun startAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}

fun STRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    // 版本兼容
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || requestCode !== PERMISSION_REQUEST_CODE)
        return
    var i = 0
    permissions.forEach {
        if (grantResults[i++] === PackageManager.PERMISSION_DENIED) {
            val showRationale = shouldShowRequestPermissionRationale(activity, it)
            if (!showRationale) {
                // 用户点击不再提醒
                startAppSettings(activity)
            } else {
                // 用户点击了取消...
                Toast.makeText(activity, it + activity.getString(R.string.permmission_tips), Toast.LENGTH_SHORT).show()
                STLog.d(activity, it + activity.getString(R.string.permmission_tips))
            }
        }
    }
}
