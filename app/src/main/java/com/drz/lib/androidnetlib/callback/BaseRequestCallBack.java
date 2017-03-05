package com.drz.lib.androidnetlib.callback;

import com.drz.lib.androidnetlib.callback.interfaces.IRequestCallBack;
import com.drz.lib.androidnetlib.entity.RequestAttributes;

/**
 * @author 杜润哲{a12345570@163.com}
 * @date 2017/2/20 0020  22:40
 */

public abstract class BaseRequestCallBack implements IRequestCallBack {
    /**
     * 请求属性
     */
    protected RequestAttributes requestAttributes;

    public RequestAttributes getRequestAttributes() {
        return requestAttributes;
    }

    @Override
    public void setRequestAttributes(RequestAttributes requestAttributes) {
        this.requestAttributes = requestAttributes;
    }
}
