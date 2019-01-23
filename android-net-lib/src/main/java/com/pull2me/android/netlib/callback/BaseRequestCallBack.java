package com.pull2me.android.netlib.callback;

import com.pull2me.android.netlib.callback.interfaces.IRequestCallBack;
import com.pull2me.android.netlib.entity.RequestAttributes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * @author 杜润哲{a12345570@163.com}
 * @date 2017/2/20 0020  22:40
 */

public abstract class BaseRequestCallBack<Q> implements IRequestCallBack<Q> {
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

    protected static String create(byte[] content) throws IOException {
        //读取字节流到内存
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        //转换为字符读取流
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
        //写入到字符输出流
        BufferedWriter bw = new BufferedWriter(new StringWriter());
        char[] charBuffer = new char[1024];
        int len = 0;
        while ((len = br.read(charBuffer)) != -1) {
            bw.write(charBuffer, 0, len);
        }
        bw.flush();
        br.close();
        bw.close();
        return bw.toString();
    }
}
