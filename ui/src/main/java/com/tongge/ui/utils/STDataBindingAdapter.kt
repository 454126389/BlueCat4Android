package com.tongge.ui.utils

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions

/**
 * Created by DZ on 2017/5/27.
 * 各种属性转DataBinding
 */

/**
 * 用databinding设置Layout_marginTop时要转一下
 * @param view view
 * @param topMargin dataBinding设置的数据
 */
@BindingAdapter("android:layout_marginTop")
fun setTopMargin(view: View, topMargin: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(layoutParams.leftMargin, topMargin.toInt(),
            layoutParams.rightMargin, layoutParams.bottomMargin)
    view.layoutParams = layoutParams
}

/**
 * 加载图片
 * @param view view
 * @param url 网络图片的地址
 * @param placeHolder 占位图ID
 * @param error 获取网络图片失败时加载的图片ID
 */
@BindingAdapter("url", "ph", "err")
fun loadImg(view: View, url: String, placeHolder: Int, error: Int) {
    val options = RequestOptions()
            .fitCenter()
            .placeholder(placeHolder)
            .error(error)
            .centerCrop()
            .transform( GlideCircleTransformation(view.context))
            .priority(Priority.NORMAL)
    Glide.with(view.context)
            .load(url)
            .`apply`(options)
            .`into`(view as ImageView)

}