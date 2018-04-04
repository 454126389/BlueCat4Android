package com.tongge.bluecat;

import android.app.Application;

import com.tongge.common.spUtils.SpUtil;
import com.tongge.common.sysUtils.AppManger;
import com.tongge.netCore.NetCore;
import com.tongge.ui.utils.AppCommonParamsUtil;

/**
 * Created by DZ on 2017/5/2.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCommonParams();

        SpUtil.init(this);

    }

    private void initCommonParams() {
        AppManger.isDebug = AppManger.isAppDebug(this, this.getPackageName());
        AppCommonParamsUtil.Companion.shareInstance().
                setLoginIcon(com.tongge.ui.R.mipmap.login_logo);
        AppCommonParamsUtil.Companion.shareInstance().
                setSuccWithNetCore(NetCore.getInstance().netCore_init() && NetCore.getInstance().PPCS_init(null));
    }

}
