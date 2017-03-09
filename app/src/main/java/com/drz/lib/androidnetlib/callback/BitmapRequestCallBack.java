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

public abstract class BitmapRequestCallBack extends BaseRequestCallBack {

    @Override
    public void onResponse(HttpResponse httpResponse) {
        Buffer buffer = new Buffer();
        Buffer buf = null;
        try {
            buf = buffer.readFrom(new ByteArrayInputStream(httpResponse.getBody()));
        } catch (IOException e) {
            onException(e);
            return;
        }
        InputStream is = buf.inputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        byte[] body = httpResponse.getBody();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(body, 0, body.length);
        onBitMapResponse(bitmap);

//        ByteArrayInputStream bais = null;
//        ByteArrayOutputStream baos = null;
//        try {
//            bais = new ByteArrayInputStream(body);
//            baos = new ByteArrayOutputStream();
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = bais.read(buf)) != -1) {
//                baos.write(buf, 0, len);
//            }
//            baos.flush();
//            Bitmap bitmap = BitmapFactory.decodeStream(bais);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            onBitMapResponse(bitmap);
//        } catch (IOException e) {
//            onException(e);
//        } finally {
//            try {
//                baos.close();
//                bais.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    protected abstract void onBitMapResponse(Bitmap bitmap);
}
