package com.tongge.ui.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by DZ on 2017/7/6.
 */
fun shortToast(context: Context?, content: String?){
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
}

fun longToast(context: Context?, content: String?){
    Toast.makeText(context, content, Toast.LENGTH_LONG).show()
}