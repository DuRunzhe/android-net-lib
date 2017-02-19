package com.drz.lib.androidnetlib.request;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.widget.Toast;

import com.drz.lib.androidnetlib.callback.BaseRequestCallBack;
import com.drz.lib.androidnetlib.entity.Header;
import com.drz.lib.androidnetlib.urlconnection.UrlConnectionResponseHandler;
import com.drz.lib.androidnetlib.urlconnection.UrlConnectionTools;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/2/19 0019.
 */
public class HttpRequestTest {
    @Test
    public void test() throws Exception {
        // Context of the app under test.
        final Context appContext = InstrumentationRegistry.getTargetContext();
        new HttpRequest.Builder(appContext)
                .get()
                .url("http://192.168.111.106:9090/user/info")
                .addParams("id", "1")
                .execute(new BaseRequestCallBack() {
                    @Override
                    public void onResponse(byte[] responseEntity) {

                    }

                    @Override
                    public void onException(Throwable e) {
                        Log.e("debug", e.getMessage());
//                        Toast.makeText(appContext, "test:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("debug", "test:" + response);
//                        Toast.makeText(appContext, "test:" + response, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Test
    public void testTool() throws Exception {
        final Context appContext = InstrumentationRegistry.getTargetContext();
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        UrlConnectionTools.get("http://192.168.111.106:9090/user/info",
                params, new UrlConnectionResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("debug", "testTool:" + new String(responseBody));
//                        Toast.makeText(appContext, "testTool:" + new String(responseBody), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("debug", "statusCode=" + statusCode);
//                        Toast.makeText(appContext, "testTool: statusCode=" + statusCode, Toast.LENGTH_SHORT).show();
                    }
                }, "", "");
    }

}