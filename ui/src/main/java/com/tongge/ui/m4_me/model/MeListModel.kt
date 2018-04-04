package com.tongge.ui.m4_me.model

import android.databinding.ObservableField
import android.databinding.ObservableInt

/**
 * Created by DZ on 2017/5/26.
 *
 */
class MeListModel {
    var headerIcon: ObservableInt = ObservableInt()
    var name: ObservableField<String> = ObservableField()
    var lv: ObservableField<String> = ObservableField()
    var headIconUrl: ObservableField<String> = ObservableField("https://img6.bdstatic.com/img/image/smallpic/weijufenseshui.jpg")
}