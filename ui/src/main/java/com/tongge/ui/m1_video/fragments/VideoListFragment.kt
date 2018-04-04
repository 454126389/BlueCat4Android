package com.tongge.ui.m1_video.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.tongge.common.sysUtils.STLog
import com.tongge.eventBus.BindMessageEvents
import com.tongge.eventBus.EventBusUtils
import com.tongge.manager.user.BCUserActionManager
import com.tongge.netCore.cameraInfo.CameraInfo
import com.tongge.netCore.protocol.NetCoreBaseCallBack
import com.tongge.netCore.protocol.NetCoreResultParams
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.databinding.FragmentVideoListBinding
import com.tongge.ui.m1_video.activitys.PlayerActivity
import com.tongge.ui.m1_video.activitys.PlayerActivity2
import com.tongge.ui.m1_video.activitys.QRScanActivity
import com.tongge.ui.m1_video.adapter.VideoListAdapter
import com.tongge.ui.m1_video.model.VIDEOLISTMODEL
import com.tongge.ui.m1_video.model.VideoListModel
import com.tongge.ui.utils.longToast
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class VideoListFragment : BaseFragment() {

    private var emtpyImg: ImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var dataList: MutableList<VideoListModel>? = null

    override fun getContentView(): Int {
        return R.layout.fragment_video_list
    }

    companion object {
        fun newInstance(): VideoListFragment {
            val fragment = VideoListFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    override fun initFLayout() {
        initUI()
        initData()
    }

    @SuppressLint("ResourceAsColor")
    private fun initUI() {
        val videoListBind = dataBinding as FragmentVideoListBinding
        emtpyImg = videoListBind.videoListEmptyImg
        emtpyImg?.visibility = View.VISIBLE
        swipeRefreshLayout = videoListBind.videoListRefresh.apply {
            setColorSchemeResources(
                    R.color.fuchsia,
                    R.color.orange,
                    R.color.lawngreen,
                    R.color.lightskyblue)
            setProgressViewEndTarget(true, 200)
        }
        dataList = mutableListOf<VideoListModel>()
        val recycleAdapter = VideoListAdapter(activity, dataList as ArrayList<VideoListModel>)
        recyclerView = videoListBind.videoListRecyclerView.apply {
            adapter = recycleAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity,
                    LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
//        addItemDecoration(DividerItemDecoration(activity,
//                DividerItemDecoration.VERTICAL))
        }
        swipeRefreshLayout?.setOnRefreshListener { loadData(recycleAdapter) }
        swipeRefreshLayout?.isRefreshing = true

        loadData(recycleAdapter)
    }

    private fun loadData(adapter: VideoListAdapter) {
        BCUserActionManager.getInstace().
                requestDevicesList(object : NetCoreBaseCallBack() {
                    override fun callback_camlist(count: Int, cameraInfo: Array<out CameraInfo>?) {
                        STLog.d(TAG, "count = $count")
                        if (count < 0 || cameraInfo == null) {
                            STLog.d(TAG, "devices list is null")
                            emtpyImg?.visibility = View.VISIBLE
                            return
                        }
                        dataList?.clear()
                        cameraInfo.forEach {
                            dataList?.add(VideoListModel(it))
                            STLog.d(TAG, it.toString())
                        }
                        emtpyImg?.visibility = View.GONE
                        adapter.notifyDataSetChanged()
                        swipeRefreshLayout?.isRefreshing = false
                    }
                })
    }

    private fun initData() {

    }

    override fun rightBtn() {
        val intent = Intent(context, PlayerActivity::class.java)
        val deviceInfo = VideoListModel()
        deviceInfo.deviceID.set("PPCS-014950-LFMTD")
        intent.putExtra(VIDEOLISTMODEL, deviceInfo)
        context.startActivity(intent)

//        val intent = Intent(activity, QRScanActivity::class.java)
//        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun messageEvent(bindMsg: BindMessageEvents){
        if (bindMsg.code == NetCoreResultParams.BindDevices.BCS_BD_ERR_SUCCESS){
            STLog.d(TAG, bindMsg.type+getString(R.string.success))
            longToast(mActivity, bindMsg.type+ getString(R.string.success))
            swipeRefreshLayout?.isRefreshing = true
            loadData(recyclerView?.adapter as VideoListAdapter)
        }else{
            STLog.e(TAG, bindMsg.type+ "  " + bindMsg.code)
            longToast(mActivity, bindMsg.type+ getString(R.string.failed))
        }
    }

}
