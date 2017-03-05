package com.drz.lib.androidnetlib.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class BitmapRequestCallBack extends BaseRequestCallBack {

    @Override
    public void onResponse(HttpResponse httpResponse) {
        byte[] body = httpResponse.getBody();
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(body);
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = bais.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.flush();
//            byte[] data = baos.toByteArray();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap bitmap = BitmapFactory.decodeStream(bais);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            onBitMapResponse(bitmap);
        } catch (IOException e) {
            onException(e);
        } finally {
            try {
                baos.close();
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void onBitMapResponse(Bitmap bitmap);
}
