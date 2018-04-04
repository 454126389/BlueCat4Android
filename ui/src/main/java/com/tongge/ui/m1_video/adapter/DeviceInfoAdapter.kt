package com.tongge.ui.m1_video.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.tongge.ui.BR
import com.tongge.ui.R
import com.tongge.ui.base.BaseRecyclerAdatper
import com.tongge.ui.base.DefaultItemMoedl
import com.tongge.ui.databinding.ItemDefualtBinding
import com.tongge.ui.m1_video.model.DeviceInfoEvent
import java.util.*

/**
 * Created by DZ on 2017/7/5.
 *
 */
class DeviceInfoAdapter(context: Context, datas: ArrayList<DefaultItemMoedl>)
    : BaseRecyclerAdatper(context, datas){

    override fun getBRID(): Int {
        return -1
    }

    override fun bindHolder(holder: BaseRecyclerAdatperHolder<ViewDataBinding>?, position: Int) {
        holder?.getBinding()?.setVariable(BR.datas, datas[position])
        holder?.getBinding()?.setVariable(BR.event, DeviceInfoEvent())
        holder?.getBinding()?.executePendingBindings()
    }

    override fun createHolder(parent: ViewGroup?, viewType: Int): ViewDataBinding {
        return  DataBindingUtil.inflate<ItemDefualtBinding>(mLayoutInflater,
                R.layout.item_defualt, parent, true)
    }


}