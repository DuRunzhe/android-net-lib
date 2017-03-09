package com.drz.lib.androidnetlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
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
import com.drz.lib.androidnetlib.urlconnection.UrlConnectionHelper;
import com.drz.lib.androidnetlib.widget.LittleLogView;
import com.drz.lib.androidnetlib.widget.LogView;
import com.drz.lib.androidnetlib.widget.ScrollLogView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private LogView mLogView;
    private LittleLogView mLtv;
    private int count = 1;
    private Button clickView;
    private ScrollLogView mSlv;
    private ImageView mIv;
    private String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUrl();
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
//                okHttpRequest(finalCount);
//                urlConnectionGet(finalCount);
            }
        });
    }

    private void initUrl() {
        urls = new String[]{
                "http://192.168.44.235:6060/resources/img/sandiantu.png",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488720355999&di=d38f51f44adb6a433dd66eb3a65495d0&imgtype=0&src=http%3A%2F%2Fs6.sinaimg.cn%2Fmw690%2F003zfOAVzy70GTXl2MR85%26690",
                "http://p0.so.qhimgs1.com/t01e4b0a12ce71d37fc.jpg",
                "http://p5.so.qhimgs1.com/bdr/_240_/t01bf9deea2f89683d5.jpg",
                "http://titanimg.titan24.com/game/20120823/3cf487b657c7f396500027e83c9847e0.jpg",
                "http://k.zol-img.com.cn/nbbbs/6786/a6785387_s.jpg",
                "http://img.hb.aicdn.com/d4a1bb24ea9afc7245da31e513daa843ea65b9ee4a1fd-G3VZvX_fw580",
                "http://img.hb.aicdn.com/a9ab7c21de0bd30fe705f3a665219b37636e80031396e-fgw184_fw658",
                "http://i1.17173cdn.com/2fhnvk/YWxqaGBf/cms3/EthAykbkwkmqsbD.jpg!a-3-640x.jpg",
                "http://images.ali213.net/picfile/pic/2014/04/29/927_20140429152054328.jpg",
                "http://attach.bbs.miui.com/forum/201504/30/204041q9b969z55cc89oc9.jpg",
                "http://www.7160.com/uploads/allimg/130909/7-130Z9201304.jpg",
                "http://image.tianjimedia.com/uploadImages/2014/127/37/3ZOEO4C10NZG.jpg",
                "http://p0.so.qhimgs1.com/bdr/_240_/t01e767afd2dbff19ac.jpg"
        };
    }

    private String getUrl() {
        Random random = new Random(SystemClock.currentThreadTimeMillis());
        int len = urls.length;
        int index = random.nextInt(len);
        return urls[index];
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
                .url("http://cute.dituhui.com/queryLayersByMapid?map_id=5035")
                .addParams("id", "1")
                .confConnectionOutTime(5 * 1000)
                .confReadOutTime(10 * 1000)
//                        .addParams("login", "drz")
//                        .addParams("password","9ABF05085DCB0322588C57A8E9AB4EED")
                .execute(
                        new StringRequestCallBack() {
                            @Override
                            public void onStringResponse(HttpResponse<String> response) {
                                Log.e("debug", "test:" + response.getBodys());
                                String string = response.string();
                                mSlv.log("count:" + (finalCount) + " " + string);
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
                .url(getUrl())
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

    private void bitmapRequest(final int count) {
        new HttpRequest.Builder(mContext)
                .get()
                .url(getUrl())
//                .url("http://p5.so.qhimgs1.com/t01e93105df8d93c2eb.jpg")
                .execute(new BitmapRequestCallBack() {
                    @Override
                    protected void onBitMapResponse(Bitmap bitmap) {
                        Log.d("debug", "count:" + count + " Bitmap:" + bitmap);
                        mIv.setImageBitmap(bitmap);
                        mSlv.log("count:" + count + "Bitmap:" + bitmap);
                    }

                    @Override
                    public void onException(Throwable e) {
                        Log.e("error", "count:" + count + "Bitmap download failed! error:" + e.getMessage());
                        mSlv.log("count:" + count + "Bitmap download failed! error:" + e.getMessage());
                    }
                });
    }

    private void okHttpRequest(final int count) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUrl())
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("error", "count:" + count + " OkHttp Bitmap download failed! error:" + e.getMessage());
                        mSlv.log("count:" + count + " OkHttp Bitmap download failed! error:" + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIv.setImageBitmap(bitmap);
                        mSlv.log("count:" + count + " Bitmap:" + bitmap);
                    }
                });
            }
        });
    }

    private void urlConnectionGet(final int count) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String requestUrl = getUrl();
                    HttpResponse<Buffer> bufferHttpResponse = UrlConnectionHelper.syncBufferGet(
                            requestUrl, null, null, null
                    );
                    InputStream is = bufferHttpResponse.getBodys().inputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIv.setImageBitmap(bitmap);
                            mSlv.log("url:" + requestUrl);
                            mSlv.log("count:" + count + " Bitmap:" + bitmap);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("error", "count:" + count + " urlConnectionGet Bitmap download failed! error:" + e.getMessage());
                            mSlv.log("count:" + count + " urlConnectionGet Bitmap download failed! error:" + e.getMessage());
                        }
                    });
                }
            }
        });
        thread.start();
    }
}
