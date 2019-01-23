package com.pull2me.android.netlib.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pull2me.android.netlib.entity.respose.HttpResponse;

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
