package com.tongge.common.customViews

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.tongge.common.R
import com.tongge.common.sysUtils.AppManger

/**
 * Created by stai on 2017/5/27.

 */
class STHudView private constructor() : View.OnClickListener {

    /**
     * 点击外面是否消失
     *    true:  点击外面消失
     *    false: 点击外面不消失
     */
    private var isOutHidden: Boolean = false
    /**
     * popupWindows
     */
    private var popupView: PopupWindow? = null
    /**
     * 显示文字的TextView
     */
    private var strView: TextView? = null

    init {
        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val hudView = inflater.inflate(R.layout.hud_load_view, null)
        /**
         * 背景的FrameLayout
         */
        val hudLayot = hudView.findViewById<FrameLayout>(R.id.hud) as FrameLayout
        /**
         * 中间带进度条和文字的布局
         */
        val progressLayout = hudView.findViewById<LinearLayout>(R.id.hud_progress_layout) as LinearLayout
        /**
         * 进度条
         */
        val progress = hudView.findViewById<ContentLoadingProgressBar>(R.id.hud_progress) as ContentLoadingProgressBar
        strView = hudView.findViewById<TextView>(R.id.hud_content) as TextView
        hudLayot.setOnClickListener(this)
        progressLayout.setOnClickListener(this)
        progress.indeterminateDrawable.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light),
                PorterDuff.Mode.MULTIPLY)
        popupView = PopupWindow(hudView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT).apply {
            isTouchable = true
            isFocusable = true
            setBackgroundDrawable(ColorDrawable())
            isOutsideTouchable = true
        }
    }

    companion object {
        /**
         * @param view  当前窗口的根布局
         * @param str   要显示的文字，如果为""，则隐藏文字
         * @param isOutHidden   点击外面是否消失
         *                          true:  点击外面消失
         *                          false: 点击外面不消失
         */
        fun show(@NonNull view: View, @Nullable str: String, isOutHidden: Boolean){
            Single.singleton.setText(str)
            Single.singleton.isOutHidden = isOutHidden
            if (Single.singleton.popupView?.isShowing!!)
                Single.singleton.popupView?.dismiss()
            Single.singleton.popupView?.showAtLocation(view, Gravity.CENTER, 0, 0)

        }

        /**
         * @param view  当前窗口的根布局
         * @param isOutHidden   点击外面是否消失
         *                          true:  点击外面消失
         *                          false: 点击外面不消失
         */
        fun show(@NonNull view: View, isOutHidden: Boolean) {
            Single.singleton.isOutHidden = isOutHidden
            Single.singleton.strView?.visibility = View.GONE
            if (Single.singleton.popupView?.isShowing!!)
                Single.singleton.popupView?.dismiss()
            Single.singleton.popupView?.showAtLocation(view, Gravity.CENTER, 0, 0)
        }

        fun dismiss() {
            Single.singleton.popupView?.dismiss()
        }

    }

    private object Single {
        val singleton = STHudView()
    }

    /**
     * 设置文字
     * @param str   要显示的文字，如果为""，则隐藏文字
     */
    private fun setText(@Nullable str: String) {
        if (str.isNotEmpty()) {
            strView?.visibility = View.VISIBLE
            strView?.text = str
        } else {
            strView?.visibility = View.GONE
        }
    }


    private fun getContext(): Context {
        return AppManger.getInstance().currentActivity()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.hud -> {
                if (isOutHidden) {
                    dismiss()
                }
            }
            R.id.hud_progress_layout -> {

            }
            else -> {

            }
        }
    }
}