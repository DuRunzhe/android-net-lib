package com.drz.lib.androidnetlib.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import okio.Buffer;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class BitmapRequestCallBack extends BaseRequestCallBack<Bitmap> {

    @Override
    public void onResponse(HttpResponse httpResponse) {
        Bitmap bitmap = BitmapFactory.decodeStream(httpResponse.inputStream());
        if (bitmap == null) {
            onException(new Exception("Bitmap is null"));
            return;
        }
        onBitMapResponse(bitmap);
    }

    protected abstract void onBitMapResponse(Bitmap bitmap);
}
