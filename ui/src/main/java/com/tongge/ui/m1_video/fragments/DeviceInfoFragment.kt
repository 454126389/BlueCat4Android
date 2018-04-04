package com.tongge.ui.m1_video.fragments

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.base.DefaultItemMoedl
import com.tongge.ui.databinding.FragmentDeviceInfoBinding
import com.tongge.ui.m1_video.adapter.DeviceInfoAdapter
import com.tongge.ui.m1_video.model.VIDEOLISTMODEL
import com.tongge.ui.m1_video.model.VideoListModel


class DeviceInfoFragment : BaseFragment() {

    private var model: VideoListModel? = null

    override fun getContentView(): Int {
        return R.layout.fragment_device_info
    }

    override fun initFLayout() {
        setTitle(model?.name?.get())
        val myBind = dataBinding as FragmentDeviceInfoBinding
        val recycle = myBind.deviceInfoRecyclerview.apply {
            addItemDecoration(DividerItemDecoration(activity,
                    DividerItemDecoration.VERTICAL))
            adapter = DeviceInfoAdapter(mActivity!!, getDatas())
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(mActivity!!,
                    LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            model = arguments.getSerializable(VIDEOLISTMODEL) as VideoListModel?
        }
    }

    companion object {
        fun newInstance(model: VideoListModel): DeviceInfoFragment {
            val fragment = DeviceInfoFragment()
            val args = Bundle()
            args.putSerializable(VIDEOLISTMODEL, model)
            fragment.arguments = args
            return fragment
        }
    }


    private fun getDatas(): ArrayList<DefaultItemMoedl>{
        val item1 = DefaultItemMoedl().apply {
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.left_about_us))
            leftStr.set(getString(R.string.device_name))
            rightStr.set(model?.name?.get())
            rightStrColor.set(mActivity?.resources!!.getColor(R.color.dimgray))
            rightIcon.set(mActivity?.resources?.getDrawable(R.mipmap.right_arrows))
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.left_camera))
            isHiddenRightStr.set(false)
        }

        val item2 = DefaultItemMoedl().apply {
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.left_about_us))
            leftStr.set(getString(R.string.device_id))
            rightStr.set(model?.deviceID?.get())
            rightStrColor.set(mActivity?.resources!!.getColor(R.color.dimgray))
            rightIcon.set(mActivity?.resources?.getDrawable(R.mipmap.right_arrows))
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.left_sn))
            isHiddenRightStr.set(false)
        }

        val item3 = DefaultItemMoedl().apply {
            leftIcon.set(mActivity?.resources?.getDrawable(R.mipmap.logout))
            leftStr.set(getString(R.string.not_use_device))
            rightStr.set(model?.deviceID?.get())
            rightIcon.set(mActivity?.resources?.getDrawable(R.mipmap.right_arrows))
            leftStrColor.set(mActivity?.resources!!.getColor(R.color.orangered))
            isHiddenRightStr.set(true)
            isGrounpHeader.set(true)
            itemFlags.set(-1)
        }
        return arrayListOf(item1, item2, item3)
    }
}
