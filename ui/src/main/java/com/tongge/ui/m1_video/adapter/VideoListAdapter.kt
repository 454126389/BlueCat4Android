package com.tongge.ui.m1_video.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.tongge.ui.BR
import com.tongge.ui.R
import com.tongge.ui.base.BaseRecyclerAdatper
import com.tongge.ui.databinding.ItemVideoListBinding
import com.tongge.ui.m1_video.model.VideoListModel

/**
 * Created by DZ on 2017/5/26.
 *
 */
class VideoListAdapter(context: Context,datas: ArrayList<VideoListModel>)
    : BaseRecyclerAdatper(context, datas){


    override fun getBRID(): Int {
        return BR.itemDatas
    }

    override fun bindHolder(holder: BaseRecyclerAdatperHolder<ViewDataBinding>?, position: Int) {
    }

    override fun createHolder(parent: ViewGroup?, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate<ItemVideoListBinding>(mLayoutInflater,
                R.layout.item_video_list, parent, true)
    }

}