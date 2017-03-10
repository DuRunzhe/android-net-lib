package com.drz.lib.androidnetlib.callback.interfaces;


import com.drz.lib.androidnetlib.entity.RequestAttributes;
import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

/**
 *
 */
public interface IRequestCallBack<Q> {
    /**
     * 请求异常回调
     *
     * @param e
     */
    void onException(Throwable e);

    /**
     * 设置请求属性
     *
     * @param requestAttributes
     */
    void setRequestAttributes(RequestAttributes requestAttributes);

    void onResponse(HttpResponse httpResponse);
}
