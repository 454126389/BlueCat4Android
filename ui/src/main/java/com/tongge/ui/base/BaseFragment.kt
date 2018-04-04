package com.tongge.ui.base

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tongge.common.sysUtils.STLog
import org.jetbrains.annotations.NotNull

abstract class BaseFragment : Fragment() {

    protected val TAG: String by lazy {
        this.javaClass.simpleName
    }
    protected var mActivity: Activity? = null
    protected var dataBinding: ViewDataBinding? = null
    var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater!!, getContentView(), container, false)
        return dataBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFLayout()
    }

    protected abstract fun initFLayout()

    @LayoutRes
    @NotNull
    protected abstract fun getContentView(): Int

    open fun rightBtn(){}

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as Activity
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            STLog.e(TAG, context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    protected fun setTitle(title: Any?) {
        if (mActivity is BaseToorbarActivity){
            (mActivity as BaseToorbarActivity).setTitleStr(title)
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun setListener(listener: OnFragmentInteractionListener){
        this.mListener = listener
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(token: String)
    }
}
