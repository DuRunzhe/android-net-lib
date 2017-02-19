package com.drz.lib.androidnetlib.callback;

import com.drz.lib.androidnetlib.callback.interfaces.IRequestCallBack;
import com.drz.lib.androidnetlib.entity.RequestAttributes;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public abstract class BaseRequestCallBack implements IRequestCallBack {
    /**
     * 请求属性
     */
    protected RequestAttributes requestAttributes;

    public RequestAttributes getRequestAttributes() {
        return requestAttributes;
    }

    public void setRequestAttributes(RequestAttributes requestAttributes) {
        this.requestAttributes = requestAttributes;
    }
}
