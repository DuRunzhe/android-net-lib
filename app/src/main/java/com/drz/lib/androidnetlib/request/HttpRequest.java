package com.drz.lib.androidnetlib.request;


import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.drz.lib.androidnetlib.callback.interfaces.IRequestCallBack;
import com.drz.lib.androidnetlib.entity.NetConfig;
import com.drz.lib.androidnetlib.entity.RequestAttributes;
import com.drz.lib.androidnetlib.entity.respose.HttpResponse;
import com.drz.lib.androidnetlib.handler.ResponseHander;
import com.drz.lib.androidnetlib.thread.DefaultThreadPool;
import com.drz.lib.androidnetlib.thread.TestThreadPool;
import com.drz.lib.androidnetlib.urlconnection.UrlConnectionHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okio.Buffer;

/**
 * 封装一次请求的所有属性
 * Created by Administrator on 2017/2/16 0016.
 */
public class HttpRequest implements Runnable {
    protected static NetConfig globalConfig = new NetConfig();
    private final ResponseHander responseHander;
    private Context context;
    private String requestUrl;
    private Map<String, String> requestParams;
    private RequestMethod method;
    private final IRequestCallBack callBack;
    protected Map<String, String> headers;
    protected Object tag;
    protected int id;
    private final RequestAttributes mRequestAttributes;
    private NetConfig netConf;

    /**
     * 初始化全局配置
     *
     * @param netConfig
     */
    public static void initializeGlobalConf(NetConfig netConfig) {
        if (netConfig == null) {
            throw new RuntimeException(HttpRequest.class.getName() + " - - -全局初始化配置null错误");
        }
        globalConfig = netConfig;
    }

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

        mRequestAttributes = new RequestAttributes(requestUrl, id, method.name(), SystemClock.currentThreadTimeMillis(), -1L, -1, null);
        if (callBack != null) {
            callBack.setRequestAttributes(mRequestAttributes);
        }
    }

    @Override
    public void run() {
        Log.e("debug", "Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
        //执行Http请求
        HttpResponse response = null;
        try {
            if (method == RequestMethod.GET) {
                response = UrlConnectionHelper.syncBufferGet(requestUrl, netConf, requestParams, headers);
            } else if (method == RequestMethod.POST) {
                response = UrlConnectionHelper.syncBufferPost(requestUrl, netConf, requestParams, headers);
            } else {
                //TODO
            }
            //设置请求结束时间
            mRequestAttributes.setEndTime(SystemClock.currentThreadTimeMillis());
            if (response != null && response.buffer() != null) {
                final HttpResponse finalResponse = response;
                responseHander.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("debug", "---------------Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
                        callBack.onResponse(finalResponse);
                    }
                });
            } else {
                callBack.onException(new Exception("Response id null exception"));
                return;
            }
        } catch (final IOException e) {
            e.printStackTrace();
            responseHander.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "-----------------Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId() + " e:" + e.getMessage());
                    if (e == null) {
                        callBack.onException(new IOException("未知异常"));
                    } else {
                        callBack.onException(e);
                    }
                }
            });
        } catch (final Exception e) {
            responseHander.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "------------------Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
                    if (e == null) {
                        callBack.onException(new Exception("未知异常"));
                    } else {
                        callBack.onException(e);
                    }
                }
            });
        }
    }

    private HttpResponse syncRun() {
        HttpResponse response = null;
        try {
            if (method == RequestMethod.GET) {
                response = UrlConnectionHelper.syncGet(requestUrl, netConf, requestParams, headers);
            } else if (method == RequestMethod.POST) {
                response = UrlConnectionHelper.syncPost(requestUrl, netConf, requestParams, headers);
            }
        } catch (Exception e) {
            response = new HttpResponse();
            response.setError(e);
            response.setResponseCode(500);
        }
        return response;
    }


    public static class Builder {
        private String url;
        private RequestMethod method = RequestMethod.GET;
        private Map<String, String> requestParams;
        private Object tag;
        private int id;
        private IRequestCallBack requestCallBack;
        protected Map<String, String> headers;
        private Context context;
        private NetConfig netConf;

        public Builder(Context context) {
            this.context = context;
            requestParams = new HashMap<>();
            headers = new HashMap<>();
            netConf = globalConfig;
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
         * @param time 毫秒
         * @return
         */
        public Builder confConnectionOutTime(int time) {
            this.netConf.setConnectionOutTime(time);
            return this;
        }

        /**
         * 毫秒
         *
         * @param time
         * @return
         */
        public Builder confReadOutTime(int time) {
            this.netConf.setReadOutTime(time);
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
            httpRequest.config(netConf);
            DefaultThreadPool.getInstance().execute(httpRequest);
//            TestThreadPool.getInstance().execute(httpRequest);
        }

        /**
         * 同步执行方法
         *
         * @return
         */
        public HttpResponse excute() {
            HttpRequest httpRequest = new HttpRequest(context, url, requestParams, method, requestCallBack, headers, tag, id);
            httpRequest.config(netConf);
            HttpResponse httpResponse = httpRequest.syncRun();
            return httpResponse;
        }
    }

    private void config(NetConfig netConf) {
        if (netConf == null) {
            netConf = globalConfig;
        }
        this.netConf = netConf;
    }
}
