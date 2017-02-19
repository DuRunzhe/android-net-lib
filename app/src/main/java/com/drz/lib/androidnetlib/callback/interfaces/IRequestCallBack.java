package com.drz.lib.androidnetlib.callback.interfaces;


import com.drz.lib.androidnetlib.entity.RequestAttributes;

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

    /**
     * 设置请求属性
     *
     * @param requestAttributes
     */
    void setRequestAttributes(RequestAttributes requestAttributes);
}
