package com.drz.lib.androidnetlib.callback;

import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.IOException;

import okio.Buffer;

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
