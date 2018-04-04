package com.tongge.ui.m1_video.activitys

import android.databinding.ViewDataBinding
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.base.BaseToorbarActivity
import com.tongge.ui.m1_video.fragments.DeviceInfoFragment
import com.tongge.ui.m1_video.model.VIDEOLISTMODEL
import com.tongge.ui.m1_video.model.VideoListModel

val DEVICE_INFO_FRAGMENT: String by lazy {
        return@lazy DeviceInfoFragment.javaClass.simpleName
}

class DeviceInfoActivity : BaseToorbarActivity(), BaseFragment.OnFragmentInteractionListener {
    override fun getContentView(): Int {
        return R.layout.activity_device_info
    }

    override fun initLayout(dataBinding: ViewDataBinding) {
        supportFragmentManager.
                beginTransaction().
                replace(R.id.device_info_container,
                        DeviceInfoFragment.newInstance(
                                intent.getSerializableExtra(VIDEOLISTMODEL) as VideoListModel)).
//                addToBackStack(DEVICE_INFO_FRAGMENT).
                commit()
    }

    override fun onFragmentInteraction(token: String) {

    }

}
