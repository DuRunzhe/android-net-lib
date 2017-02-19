package com.drz.lib.androidnetlib.request;


import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.drz.lib.androidnetlib.callback.interfaces.IRequestCallBack;
import com.drz.lib.androidnetlib.handler.ResponseHander;
import com.drz.lib.androidnetlib.thread.DefaultThreadPool;
import com.drz.lib.androidnetlib.thread.TestThreadPool;
import com.drz.lib.androidnetlib.urlconnection.UrlConnectionHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装一次请求的所有属性
 * Created by Administrator on 2017/2/16 0016.
 */
public class HttpRequest implements Runnable {
    private final ResponseHander responseHander;
    private Context context;
    private String requestUrl;
    private Map<String, String> requestParams;
    private RequestMethod method;
    private final IRequestCallBack callBack;
    protected Map<String, String> headers;
    protected Object tag;
    protected int id;

    private HttpRequest(Context context, String requestUrl, Map<String, String> requestParams, RequestMethod method, IRequestCallBack callBack, Map<String, String> headers, Object tag, int id) {
        this.context = context;
        this.requestUrl = requestUrl;
        //接收请求数据
        this.requestParams = requestParams;
        this.method = method;
        this.callBack = callBack;
        this.headers = headers;
        this.tag = tag;
        this.id = id;
        Looper mainLooper = context.getMainLooper();
        responseHander = new ResponseHander(mainLooper);
    }

    @Override
    public void run() {
        Log.e("debug", "Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
        //执行Http请求
        String response = null;
        try {
            if (method == RequestMethod.GET) {
                response = UrlConnectionHelper.doGet(requestUrl, requestParams, headers);
            } else if (method == RequestMethod.POST) {
                response = UrlConnectionHelper.doPost(requestUrl, requestParams, headers);
            } else {

            }
            if (response != null && response.trim().length() > 0) {
                final String finalResponse = response;
                responseHander.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("debug", "---------------Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
                        callBack.onResponse(finalResponse);
                    }
                });
            }
        } catch (final IOException e) {
            e.printStackTrace();
            responseHander.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "-----------------Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId() + " e:" + e.getMessage());
                    callBack.onException(e);
                }
            });
        } catch (final Exception e) {
            responseHander.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "------------------Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
                    callBack.onException(e);
                }
            });
        }
    }


    public static class Builder {
        private String url;
        private RequestMethod method;
        private Map<String, String> requestParams;
        private Object tag;
        private int id;
        private IRequestCallBack requestCallBack;
        protected Map<String, String> headers;
        private Context context;

        public Builder(Context context) {
            this.context = context;
            requestParams = new HashMap<>();
            headers = new HashMap<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            this.method = RequestMethod.GET;
            return this;
        }

        public Builder post() {
            this.method = RequestMethod.POST;
            return this;
        }

        public Builder addParams(String key, String value) {
            requestParams.put(key, value);
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        /**
         * 执行请求
         *
         * @param requestCallBack
         * @return
         */
        public void execute(IRequestCallBack requestCallBack) {
            this.requestCallBack = requestCallBack;
            HttpRequest httpRequest = new HttpRequest(context, url, requestParams, method, requestCallBack, headers, tag, id);
            DefaultThreadPool.getInstance().execute(httpRequest);
//            TestThreadPool.getInstance().execute(httpRequest);
        }
    }
}
