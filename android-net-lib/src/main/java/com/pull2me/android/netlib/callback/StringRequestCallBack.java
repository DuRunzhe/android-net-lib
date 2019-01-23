package com.pull2me.android.netlib.callback;

import com.pull2me.android.netlib.entity.respose.HttpResponse;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class StringRequestCallBack extends BaseRequestCallBack<String> {

    @Override
    public void onResponse(HttpResponse httpResponse) {
        try {
            onStringResponse(httpResponse.string());
        } catch (Exception e) {
            onException(e);
        }
    }

    public abstract void onStringResponse(String response);
}
