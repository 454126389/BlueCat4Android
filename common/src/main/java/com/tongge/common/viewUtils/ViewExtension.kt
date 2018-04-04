package com.tongge.common.viewUtils

import android.view.View

/**
 * Created by DZ on 2017/6/12.
 * 设置View的宽高，
 * @param width     要设置的宽，<0时，为原来值
 * @param height    要设置的高，<0时，为原来值
 */
 fun View.setSize(width:Int, height:Int){
    val params = this.layoutParams
    if (width >= 0) params.width = width
    if (height >= 0) params.height = height
    this.layoutParams = params
}