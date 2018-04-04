package com.tongge.ui.utils

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * Created by DZ on 2017/5/25.
 * 反射工具类
 */

/**
 * ViewPager扩展方法
 * 用于自定义ViewPager setCurrentItem时，滑动的速度
 *
 * @param times 默认速度的倍数(默认最长只有600ms)
 */
fun ViewPager.slideSpeed(times: Int) {
    try {
        var scrollerField = this.javaClass.getDeclaredField("mScroller")
        scrollerField.isAccessible = true
        var interpolator = this.javaClass.getDeclaredField("sInterpolator")
        interpolator.isAccessible = true
        var scroller = object : Scroller(this.context, interpolator.get(null) as Interpolator) {
            override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
                super.startScroll(startX, startY, dx, dy, duration * times)
            }
        }
        scrollerField.set(this, scroller)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 去掉BottomNavigationView底部样式
 * 默认底部超过3个item时，被选中的会占用很大的空间
 *
 */
@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    try {
        val menuViews = this.getChildAt(0) as BottomNavigationMenuView
        val shiftingMode = menuViews.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuViews, false)
        shiftingMode.isAccessible = false

        for (subView in 0..menuViews.childCount-1){
            val itemView = menuViews.getChildAt(subView) as BottomNavigationItemView
            itemView.setShiftingMode(false)
            itemView.setChecked(itemView.itemData.isChecked);
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}