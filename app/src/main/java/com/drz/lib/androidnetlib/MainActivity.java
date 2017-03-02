package com.drz.lib.androidnetlib;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.drz.lib.androidnetlib.callback.BaseRequestCallBack;
import com.drz.lib.androidnetlib.request.HttpRequest;
import com.drz.lib.androidnetlib.widget.LittleLogView;
import com.drz.lib.androidnetlib.widget.LogView;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private LogView mLogView;
    private LittleLogView mLtv;
    private int count=1;

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
//        mLogView = (LogView) findViewById(R.id.lgv);
        mLtv = (LittleLogView) findViewById(R.id.ltv);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int index = 0; index < 10000; index++) {
//                    SystemClock.sleep(10);
//                    final int finalIndex = index;
////                    runOnUiThread();
//                    mLogView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mLogView != null) {
////                                mLogView.log("- - - - - - - - - " + finalIndex + "");
//                                mLtv.log("- - - - - - - - - " + finalIndex + "");
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();

        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpRequest.Builder(mContext)
                        .get()
                        .url("http://192.168.111.106:9090/user/info")
                        .addParams("id", "1")
                        .confConnectionOutTime(5 * 1000)
                        .confReadOutTime(10 * 1000)
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
                                mLtv.log("count:" + (count++) + " " + e.getMessage());
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e("debug", "test:" + response);
                                mLtv.log("count:" + (count++) + " " + response);
                                Toast.makeText(mContext, "test:" + response, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
