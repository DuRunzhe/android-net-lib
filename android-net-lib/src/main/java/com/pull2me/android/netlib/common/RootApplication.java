package com.pull2me.android.netlib.common;

import android.app.Application;

import com.pull2me.android.netlib.entity.NetConfig;
import com.pull2me.android.netlib.request.HttpRequest;

/**
 * Created by drz on 2017/3/3.
 */

public class RootApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetConfig netConfig = new NetConfig();
        netConfig.setReadOutTime(10 * 1000);
        netConfig.setConnectionOutTime(3 * 1000);
        HttpRequest.initializeGlobalConf(netConfig);
    }
}
