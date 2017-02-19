package com.drz.lib.androidnetlib;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drz.lib.androidnetlib.callback.BaseRequestCallBack;
import com.drz.lib.androidnetlib.request.HttpRequest;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpRequest.Builder(mContext)
                        .get()
                        .url("http://192.168.111.106:9090/user/info")
                        .addParams("id", "1")
//                        .addParams("login", "drz")
//                        .addParams("password","9ABF05085DCB0322588C57A8E9AB4EED")
                        .execute(new BaseRequestCallBack() {
                            @Override
                            public void onResponse(byte[] responseEntity) {

                            }

                            @Override
                            public void onException(Throwable e) {
                                Log.e("debug", e.getMessage());
                                Toast.makeText(mContext, "test:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e("debug", "test:" + response);
                                Toast.makeText(mContext, "test:" + response, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
