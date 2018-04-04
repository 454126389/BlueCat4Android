package com.tongge.ui.m4_me.model

import android.view.View
import com.tongge.ui.base.DefaultItemEvent
import com.tongge.ui.base.DefaultItemMoedl
import com.tongge.ui.utils.shortToast

/**
 * Created by DZ on 2017/7/5.
 *
 */
class MeListEvent : DefaultItemEvent {

    override fun defaultItemClick(view: View, model: DefaultItemMoedl) {
        shortToast(view.context, model.leftStr.get())
    }
}
