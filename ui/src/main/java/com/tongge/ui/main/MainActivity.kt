package com.tongge.ui.main

import android.databinding.ViewDataBinding
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import com.tongge.common.sysUtils.AppManger
import com.tongge.ui.R
import com.tongge.ui.base.BaseFragment
import com.tongge.ui.base.BaseToorbarActivity
import com.tongge.ui.databinding.ActivityMainBinding
import com.tongge.ui.m1_video.fragments.VideoListFragment
import com.tongge.ui.m2_message.fragments.MessageListFragment
import com.tongge.ui.m3_discover.fragments.DiscoverListFragment
import com.tongge.ui.m4_me.fragments.MeListFragment
import com.tongge.ui.utils.disableShiftMode
import com.tongge.ui.utils.shortToast

class MainActivity : BaseToorbarActivity(), ViewPager.OnPageChangeListener, BaseFragment.OnFragmentInteractionListener {

    private var bottomView: BottomNavigationView? = null
    private var pager: ViewPager? = null
    private var isExitApp = false

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun initLayout(dataBinding: ViewDataBinding) {
        setTitleStr(R.string.video)
        hiddenBackBtn(true)
        rightbtn?.apply {
            text = resources.getString(R.string.add)
            visibility = View.VISIBLE
            tag = 0
        }
        val mainBind = dataBinding as ActivityMainBinding
        bottomView = mainBind.bottomNavigation
        pager = mainBind.mainViewpager

        window.decorView.viewTreeObserver
        initBottomView()
        initFragments()
    }


    private fun initBottomView() {
        bottomView?.disableShiftMode()
        bottomView?.setOnNavigationItemSelectedListener {
            pager?.setCurrentItem(it.order, false)
            true
        }
    }

    private fun initFragments() {
        val fragments = ArrayList<Fragment>().apply {
            add(VideoListFragment.newInstance())
            add(DiscoverListFragment.newInstance())
            add(MessageListFragment.newInstance())
            add(MeListFragment.newInstance())
        }
        pager?.adapter = MainPagerAdatper(supportFragmentManager, fragments)
        pager?.currentItem = 0
        pager?.offscreenPageLimit = fragments.size
        pager?.addOnPageChangeListener(this)

    }

    override fun onPageScrollStateChanged(p0: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(p0: Int) {
        bottomView?.menu?.getItem(p0)?.isChecked = true
        rightbtn?.tag = p0
        when (p0) {
        //视频
            0 -> {
                setTitleStr(this.resources.getString(R.string.video))
                rightbtn?.visibility = View.VISIBLE
            }
        //消息
            1 -> {
                setTitleStr(this.resources.getString(R.string.message))
                rightbtn?.visibility = View.GONE
            }
        //发现
            2 -> {
                setTitleStr(this.resources.getString(R.string.discover))
                rightbtn?.visibility = View.GONE
            }
        //我
            3 -> {
                setTitleStr(this.resources.getString(R.string.me))
                rightbtn?.visibility = View.GONE
            }
        }
    }

    override fun rightBtnClick() {
        val adpter = pager?.adapter as MainPagerAdatper
        val fragment = adpter.fragments?.get(rightbtn?.tag as Int) as BaseFragment
        fragment.rightBtn()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (isExitApp)
                    AppManger.getInstance().AppExit(this)
                else{
                    shortToast(this,getString(R.string.double_exit_app))
                    isExitApp = true
                    Handler().postDelayed({isExitApp = false},2000)
                }
                return true
            }
            else -> return super.onKeyUp(keyCode, event)
        }

    }

    override fun onFragmentInteraction(token: String) {

    }

}
