package com.drz.lib.androidnetlib.callback;

import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.IOException;

import okio.Buffer;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class StringRequestCallBack extends BaseRequestCallBack<Buffer> {

    @Override
    public void onResponse(HttpResponse<Buffer> httpResponse) {
        try {
            String bodys = httpResponse.string();
            HttpResponse<String> response = new HttpResponse<>();
            response.setBody(httpResponse.getBody());
            response.setBodys(bodys);
            response.setContentLength(httpResponse.getContentLength());
            response.setError(httpResponse.getError());
            onStringResponse(response);
        } catch (Exception e) {
            onException(e);
        }
    }

    public abstract void onStringResponse(HttpResponse<String> response);
}
