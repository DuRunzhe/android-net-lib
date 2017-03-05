package com.drz.lib.androidnetlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.drz.lib.androidnetlib.callback.BitmapRequestCallBack;
import com.drz.lib.androidnetlib.callback.FileRequestCallBack;
import com.drz.lib.androidnetlib.callback.StringRequestCallBack;
import com.drz.lib.androidnetlib.entity.respose.HttpResponse;
import com.drz.lib.androidnetlib.request.HttpRequest;
import com.drz.lib.androidnetlib.widget.LittleLogView;
import com.drz.lib.androidnetlib.widget.LogView;
import com.drz.lib.androidnetlib.widget.ScrollLogView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private LogView mLogView;
    private LittleLogView mLtv;
    private int count = 1;
    private Button clickView;
    private ScrollLogView mSlv;
    private ImageView mIv;

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
        mSlv = (ScrollLogView) findViewById(R.id.slv);
        mIv = (ImageView) findViewById(R.id.iv);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int index = 0; index < 100; index++) {
//                    SystemClock.sleep(100);
//                    final int finalIndex = index;
////                    runOnUiThread();
//                    mSlv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mSlv != null) {
////                                mLogView.log("- - - - - - - - - " + finalIndex + "");
////                                mLtv.log("- - - - - - - - - " + finalIndex + "");
//                                mSlv.log("- - - - - - - - - " + finalIndex);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();

        clickView = (Button) findViewById(R.id.bt_test);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int finalCount = count++;
                clickView.setText("连接" + finalCount);
//                asyncRequest(finalCount);
//                syncRequest(finalCount);
                fileRequest(finalCount);
//                bitmapRequest(finalCount);
            }
        });
    }

    private void syncRequest(final int finalCount) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpResponse response = new HttpRequest.Builder(mContext)
                        .get()
                        .url("http://192.168.111.106:9090/user/info")
                        .addParams("id", "1")
//                        .confConnectionOutTime(5 * 1000)
//                        .confReadOutTime(10 * 1000)
//                        .addParams("login", "drz")
//                        .addParams("password","9ABF05085DCB0322588C57A8E9AB4EED")
                        .excute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSlv.log("count:" + (finalCount) + " body:" + response.string() + ", error:" + response.getError().toString());
                    }
                });
            }
        });
        thread.start();
    }

    private void asyncRequest(final int finalCount) {
        new HttpRequest.Builder(mContext)
                .get()
                .url("http://192.168.111.106:9090/user/info")
                .addParams("id", "1")
//                        .confConnectionOutTime(5 * 1000)
//                        .confReadOutTime(10 * 1000)
//                        .addParams("login", "drz")
//                        .addParams("password","9ABF05085DCB0322588C57A8E9AB4EED")
                .execute(
                        new StringRequestCallBack() {
                            @Override
                            public void onStringResponse(HttpResponse<String> response) {
                                Log.e("debug", "test:" + response.getBodys());
                                String string = response.string();
                                mLtv.log("count:" + (finalCount) + " " + string);
                                Toast.makeText(mContext, "test:" + string, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onException(Throwable e) {
                                Log.e("debug", e.getMessage());
                                Toast.makeText(mContext, "test:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                mLtv.log("count:" + (finalCount) + " " + e.getMessage());
                                mSlv.log("count:" + (finalCount) + " " + e.getMessage());
                            }
                        }
                );
    }

    private void fileRequest(int count) {
        new HttpRequest.Builder(mContext)
                .get()
                .url("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488720355999&di=d38f51f44adb6a433dd66eb3a65495d0&imgtype=0&src=http%3A%2F%2Fs6.sinaimg.cn%2Fmw690%2F003zfOAVzy70GTXl2MR85%26690")
                .execute(new FileRequestCallBack(Environment.getExternalStorageDirectory().getPath(), "test2.jpeg") {
                    @Override
                    protected void onFileResponse(HttpResponse<File> httpResponse) {
                        File file = httpResponse.getBodys();
                        Log.d("debug", "File Name:" + file.getName() + ", length:" + String.valueOf(file.length()));
                        mSlv.log("File Name:" + file.getName() + ", length:" + String.valueOf(file.length()));
                        mIv.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                    }

                    @Override
                    public void onException(Throwable e) {
                        Log.e("error", "File Download failed! error:" + e.getMessage());
                        mSlv.log("File Download failed! error:" + e.getMessage());
                    }
                });
    }

    private void bitmapRequest(int count) {
        new HttpRequest.Builder(mContext)
                .get()
                .url("http://p0.so.qhimgs1.com/t01e4b0a12ce71d37fc.jpg")
//                .url("http://p5.so.qhimgs1.com/t01e93105df8d93c2eb.jpg")
                .execute(new BitmapRequestCallBack() {
                    @Override
                    protected void onBitMapResponse(Bitmap bitmap) {
                        Log.d("debug", "Bitmap:" + bitmap);
                        mIv.setImageBitmap(bitmap);
                        mSlv.log("Bitmap:" + bitmap);
                    }

                    @Override
                    public void onException(Throwable e) {
                        Log.e("error", "Bitmap download failed! error:" + e.getMessage());
                        mSlv.log("Bitmap download failed! error:" + e.getMessage());
                    }
                });
    }

}
