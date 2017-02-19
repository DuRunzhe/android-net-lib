package com.drz.lib.androidnetlib.urlconnection;

import com.drz.lib.androidnetlib.entity.Header;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class UrlConnectionTools {

    private static HttpURLConnection getHttpURLConnection(String urlStr,
                                                          String ticket,
                                                          String token) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            // urlConnection.setRequestProperty("ticket", ticket);
            // urlConnection.setRequestProperty("token", token);
        } catch (MalformedURLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urlConnection;

    }

    public static void get(String url,
                           Map<String, String> params,
                           UrlConnectionResponseHandler handler,
                           String ticket,
                           String token) {
        HttpURLConnection urlConnection = getHttpURLConnection(url, ticket,
                token);
        try {
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            // urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestMethod("GET");
            urlConnection
                    .setRequestProperty(
                            "user-agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            // urlConnection.setRequestProperty("Host", "api.map.baidu.com");
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlConnection.setRequestProperty(key, value);
            }

            urlConnection.connect();

            int code = urlConnection.getResponseCode();
            // Map<String, List<String>> headerFields =
            // urlConnection.getHeaderFields();
            if (code == 200) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream in = urlConnection.getInputStream();
                byte[] buf = new byte[1024];
                int len = -1;
                while ((len = in.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                    baos.flush();
                }
                ioClose(in);
                ioClose(baos);

                byte[] byteArray = baos.toByteArray();
                Header[] headers = null;
                // 请求成功
                handler.onSuccess(code, headers, byteArray);
            } else {
                handler.onFailure(code, null, null, null);
            }
        } catch (ProtocolException e) {
            hadleError(handler, urlConnection, e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            hadleError(handler, urlConnection, e);
        }
    }

    public static void post(String url, Map<String, String> params,
                            UrlConnectionResponseHandler handler, String ticket, String token) {
        HttpURLConnection urlConnection = getHttpURLConnection(url, ticket,
                token);
        try {
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(false);
            // urlConnection.setRequestProperty("Content-Type",
            // "application/x-www-form-urlencoded");
            // urlConnection.setRequestProperty("Content-Type", "form-data");

            // DataOutputStream out = new
            // DataOutputStream(urlConnection.getOutputStream());
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlConnection.setRequestProperty(key, value);
            }

            urlConnection.connect();
            // out.flush();
            // out.close();

            int code = urlConnection.getResponseCode();
            // Map<String, List<String>> headerFields =
            // urlConnection.getHeaderFields();
            if (code == 200) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream in = urlConnection.getInputStream();
                byte[] buf = new byte[1024];
                int len = -1;
                while ((len = in.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                    baos.flush();
                }
                ioClose(in);
                ioClose(baos);

                byte[] byteArray = baos.toByteArray();
                Header[] headers = null;
                // 请求成功
                handler.onSuccess(code, headers, byteArray);
            } else {
                handler.onFailure(code, null, null, null);
            }
        } catch (ProtocolException e) {
            hadleError(handler, urlConnection, e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            hadleError(handler, urlConnection, e);
        }
    }

    private static void hadleError(UrlConnectionResponseHandler handler,
                                   HttpURLConnection urlConnection, IOException e) {
        e.printStackTrace();
        int code = -1;
        try {
            code = urlConnection.getResponseCode();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        handler.onFailure(code, null, null, e);
    }

    private static void ioClose(Closeable baos) {
        if (baos != null) {
            try {
                baos.close();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
