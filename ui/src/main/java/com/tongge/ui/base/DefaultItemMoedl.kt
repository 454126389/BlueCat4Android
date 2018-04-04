package com.tongge.ui.base

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.graphics.Color
import android.graphics.drawable.Drawable

/**
 * Created by DZ on 2017/5/27.
 * 通用list的item，下面的鬼样子
 * -----------------------------------------------
 * |      |              |                |       |
 * | 图标  | 标题         |            内容 |   箭头 |
 * |      |              |                |       |
 * -----------------------------------------------
 */
class DefaultItemMoedl {
    /* 最左边图像 */
    var leftIcon: ObservableField<Drawable> = ObservableField()
    /* 左边标题 */
    var leftStr: ObservableField<String> = ObservableField()
    /* 左边文字颜色，默认：黑色 */
    var leftStrColor: ObservableInt = ObservableInt(Color.BLACK)
    /* 右边文字内容 */
    var rightStr: ObservableField<String> = ObservableField()
    /* 右边文字颜色，默认：灰色 */
    var rightStrColor: ObservableInt = ObservableInt(Color.GRAY)
    /* 右边箭头的图像 */
    var rightIcon: ObservableField<Drawable> = ObservableField()
    /* 是否是分组的第一个，如果是，在布局文件中设置marginTop大一些 */
    var isGrounpHeader: ObservableBoolean = ObservableBoolean(false)
    /* 是否隐藏最左边图像 */
    var isHiddenLeftIcon: ObservableBoolean = ObservableBoolean(false)
    /* 是否隐藏最右边图像 */
    var isHiddenRightIcon: ObservableBoolean = ObservableBoolean(false)
    /* 是否隐藏最右边文字 */
    var isHiddenRightStr: ObservableBoolean = ObservableBoolean(false)
    /* 用于标记该item */
    var itemFlags: ObservableInt = ObservableInt(0)

}