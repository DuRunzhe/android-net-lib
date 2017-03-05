package com.drz.lib.androidnetlib.callback;

import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class StringRequestCallBack extends BaseRequestCallBack<String> {

    @Override
    public void onResponse(HttpResponse<String> httpResponse) {
        try {
            String response = create(httpResponse.getBody());
            httpResponse.setBodys(response);
            onStringResponse(httpResponse);
        } catch (IOException e) {
            onException(e);
        }
    }

    public abstract void onStringResponse(HttpResponse<String> response);
}
