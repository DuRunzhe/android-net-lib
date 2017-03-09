package com.drz.lib.androidnetlib.entity.respose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;

import okio.Buffer;

/**
 * Created by drz on 2017/3/3.
 */

public class HttpResponse<Q> implements Serializable {
    private byte[] body;
    private int responseCode;
    private Throwable error;
    private Q bodys;
    private long contentLength;
    private Buffer buffer;

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public String string() {
        if (buffer != null) {
            return buffer.readUtf8();
        }
        return null;
    }

    private static String create(byte[] content) throws IOException {
        //读取字节流到内存
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        //转换为字符读取流
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
        //写入到字符输出流
        BufferedWriter bw = new BufferedWriter(new StringWriter());
        char[] charBuffer = new char[1024];
        int len;
        while ((len = br.read(charBuffer)) != -1) {
            bw.write(charBuffer, 0, len);
        }
        bw.flush();
        br.close();
        bw.close();
        return bw.toString();
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public HttpResponse() {
    }

    public byte[] getBody() {
        if (body == null) {
            body = bytes();
        }
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Q getBodys() {
        return bodys;
    }

    public void setBodys(Q bodys) {
        this.bodys = bodys;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] bytes() {
        if (buffer != null) {
            return buffer.readByteArray();
        }
        return null;
    }

    public InputStream inputStream() {
        if (buffer != null) {
            return buffer.inputStream();
        }
        return null;
    }
}
