package com.tongge.ui.m4_me.fragments

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.base.DefaultItemMoedl
import com.tongge.ui.databinding.FragmentMeListBinding
import com.tongge.ui.m4_me.adapter.MeListAdatper
import com.tongge.ui.m4_me.model.MeListModel

class MeListFragment : BaseFragment() {

    override fun getContentView(): Int {
        return R.layout.fragment_me_list
    }

    companion object {
        fun newInstance(): MeListFragment {
            val fragment = MeListFragment()
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initFLayout() {

        val myBind = dataBinding as FragmentMeListBinding
        val headInfo = MeListModel().apply {
            headerIcon.set(R.mipmap.no_img)
            name.set("10086")
            lv.set(activity.getString(R.string.lv1))
        }
        myBind.selfInfo = headInfo

        val item1 = DefaultItemMoedl().apply {
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.left_about_us))
            leftStr.set(mActivity?.getString(R.string.about_us))
            rightIcon.set(mActivity?.resources?.getDrawable(R.mipmap.right_arrows))
            isHiddenRightStr.set(true)
        }

        val item2 = DefaultItemMoedl().apply {
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.logout))
            leftStr.set(mActivity?.getString(R.string.logout))
            rightIcon.set(mActivity?.resources?.getDrawable(R.mipmap.right_arrows))
            leftStrColor.set(mActivity?.resources!!.getColor(R.color.orangered))
            isHiddenRightStr.set(true)
            isGrounpHeader.set(true)
        }

        val recycleDataList = arrayListOf<DefaultItemMoedl>(item1, item2)

        val meListAdapter = MeListAdatper(mActivity!!,recycleDataList)
        val recycle = myBind.meListRecycle.apply {
            addItemDecoration(DividerItemDecoration(activity,
                    DividerItemDecoration.VERTICAL))
            adapter = meListAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(mActivity!!,
                    LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }

    }
}
