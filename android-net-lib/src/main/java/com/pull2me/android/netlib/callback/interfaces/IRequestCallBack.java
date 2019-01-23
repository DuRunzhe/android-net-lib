package com.pull2me.android.netlib.callback.interfaces;


import com.pull2me.android.netlib.entity.RequestAttributes;
import com.pull2me.android.netlib.entity.respose.HttpResponse;

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
