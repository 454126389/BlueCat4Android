package com.tongge.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tongge.ui.R

abstract class BaseToorbarActivity : BaseActivity() {

    var titleView: TextView? = null
    var emptyImg: ImageView? = null
    protected var rightbtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_toorbar)
        val toolbar = findViewById(R.id.tool_bar) as Toolbar
        titleView = findViewById(R.id.activity_title) as TextView
        emptyImg = findViewById(R.id.ac_empty_img) as ImageView
        rightbtn = findViewById(R.id.tool_bar_right_btn) as Button
        rightbtn?.setOnClickListener({
            rightBtnClick()
        })
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        val contentView = findViewById(R.id.content_view) as FrameLayout
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        initLayout(DataBindingUtil.inflate(layoutInflater, getContentView(), contentView, true))
    }

    protected abstract fun initLayout(@NonNull dataBinding: ViewDataBinding)
    @LayoutRes
    @NonNull
    protected abstract fun getContentView(): Int

    open protected fun rightBtnClick(){}

    fun setTitleStr(title: Any?) {
        when (title) {
            is String -> titleView?.setText(title)
            is Int -> titleView?.setText(title)
            else -> titleView?.setText("")
        }
    }

    protected fun hiddenBackBtn(hidden: Boolean) {
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(!hidden)
        }
    }

    protected fun showEmptyImg(show: Boolean) {
        emptyImg?.visibility = if (show) View.VISIBLE else View.GONE
    }
}
