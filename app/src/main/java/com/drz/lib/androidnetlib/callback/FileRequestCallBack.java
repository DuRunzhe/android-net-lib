package com.drz.lib.androidnetlib.callback;

import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 请求文件回调
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class FileRequestCallBack extends BaseRequestCallBack<File> {
    private String mDestFileDir;
    private String mFileName;

    public FileRequestCallBack(String destFileDir, String fileName) {
        mDestFileDir = destFileDir;
        mFileName = fileName;
    }

    @Override
    public void onResponse(HttpResponse<File> responseEntity) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            int sum = 0;
            is = responseEntity.inputStream();
            File dir = new File(mDestFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, mFileName);
            fos = new FileOutputStream(file);
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
            }
            fos.flush();
            responseEntity.setContentLength(sum);
            responseEntity.setBodys(file);
            onFileResponse(responseEntity);
        } catch (IOException e) {
            onException(e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }

        }

    }

    protected abstract void onFileResponse(HttpResponse<File> httpResponse);
}
