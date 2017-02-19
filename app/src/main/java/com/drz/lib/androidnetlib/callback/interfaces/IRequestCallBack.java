package com.drz.lib.androidnetlib.callback.interfaces;


/**
 *
 */
public interface IRequestCallBack {
    /**
     * 响应回调
     *
     * @param responseEntity
     */
    void onResponse(byte[] responseEntity);

    /**
     * 请求异常回调
     *
     * @param e
     */
    void onException(Throwable e);

    /**
     * 响应回调
     *
     * @param response
     */
    void onResponse(String response);
}
