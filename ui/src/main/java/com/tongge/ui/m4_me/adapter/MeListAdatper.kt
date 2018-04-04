package com.tongge.ui.m4_me.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.tongge.ui.BR
import com.tongge.ui.R
import com.tongge.ui.base.BaseRecyclerAdatper
import com.tongge.ui.base.DefaultItemMoedl
import com.tongge.ui.databinding.ItemDefualtBinding
import com.tongge.ui.m4_me.model.MeListEvent

/**
 * Created by DZ on 2017/5/26.
 *
 */
class MeListAdatper(context: Context, datas: ArrayList<DefaultItemMoedl>)
    : BaseRecyclerAdatper(context, datas) {

    override fun getBRID(): Int {
        return -1
    }

    override fun bindHolder(holder: BaseRecyclerAdatperHolder<ViewDataBinding>?, position: Int) {
        holder?.getBinding()?.setVariable(BR.datas, datas[position])
        holder?.getBinding()?.setVariable(BR.event, MeListEvent())
        holder?.getBinding()?.executePendingBindings()
    }

    override fun createHolder(parent: ViewGroup?, viewType: Int): ViewDataBinding {
        return  DataBindingUtil.inflate<ItemDefualtBinding>(mLayoutInflater,
                R.layout.item_defualt, parent, true)
    }

}