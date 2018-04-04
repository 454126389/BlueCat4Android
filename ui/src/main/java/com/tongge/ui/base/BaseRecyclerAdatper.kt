package com.tongge.ui.base

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tongge.ui.R

/**
 * Created by DZ on 2017/7/5.
 *
 */
abstract class BaseRecyclerAdatper (val context: Context, var datas: ArrayList<*>)
    : RecyclerView.Adapter<BaseRecyclerAdatper.BaseRecyclerAdatperHolder<ViewDataBinding>>(){

    protected var mLayoutInflater: LayoutInflater? = null
    init {
        this.mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: BaseRecyclerAdatperHolder<ViewDataBinding>?, position: Int) {
        val id = getBRID()
        if (id <= 0)
            bindHolder(holder, position)
        else {
            holder?.getBinding()?.setVariable(id, datas[position])
            holder?.getBinding()?.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerAdatperHolder<ViewDataBinding> {
        return BaseRecyclerAdatperHolder(createHolder(parent, viewType))
    }

    /**
     * 如果布局中的<data></data>中只有一个variabl
     *
     * @return
     *      如果布局中的<data></data>中只有一个variabl，直接返回BR.id<Br/>
     *      如果有多个，请返回一个 <= 0 的整数，会调用bindHolder方法
     *      View
     */
    @IntegerRes
    abstract fun getBRID(): Int

    /**
     * 如果布局中的<data></data>中有多个个variabl
     */
    abstract fun bindHolder(holder: BaseRecyclerAdatperHolder<ViewDataBinding>?, position: Int)
    abstract fun createHolder(parent: ViewGroup?, viewType: Int): ViewDataBinding

    class BaseRecyclerAdatperHolder<out T: ViewDataBinding>
        : RecyclerView.ViewHolder {
        private var binding: T? = null
        constructor(bind: T) : super(bind.root) {
            if (bind is ViewDataBinding)
                this.binding = bind
            else
                throw (Exception(bind.root.context.getString(R.string.adapter_holder_params_err)))
        }

        fun getBinding(): T? {
            return this.binding
        }
    }
}