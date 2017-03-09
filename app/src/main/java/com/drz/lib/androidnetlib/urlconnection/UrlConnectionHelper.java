package com.drz.lib.androidnetlib.urlconnection;

import com.drz.lib.androidnetlib.entity.NetConfig;
import com.drz.lib.androidnetlib.entity.respose.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okio.Buffer;

/**
 * Created by drz on 2016/2/24.
 */
public class UrlConnectionHelper {
    public UrlConnectionHelper() {
        super();
    }

    public static void config(HttpURLConnection urlConnection, NetConfig netConfig) {
        urlConnection.setConnectTimeout(netConfig.getConnectionOutTime());
        urlConnection.setReadTimeout(netConfig.getReadOutTime());
    }

    /**
     * @param requestUrl
     * @param netConf
     * @param params
     * @param headers    @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static String doGet(String requestUrl, NetConfig netConf, Map<String, String> params, Map<String, String> headers) throws IOException, Exception {
        String s = urlEncode(params);
        requestUrl += "?" + s;
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(false);//GET方式时需要保持false
        urlConnection.setConnectTimeout(30 * 1000);
        urlConnection.setReadTimeout(60 * 1000);
//        urlConnection.setUseCaches(false);
//        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(netConf.getConnectionOutTime());
        urlConnection.setReadTimeout(netConf.getReadOutTime());
        initHeader(headers, urlConnection);


        urlConnection.connect();
        int code = urlConnection.getResponseCode();
        if (code == 200) {
            InputStream is = urlConnection.getInputStream();
            StringBuilder responseSb = null;
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                responseSb = new StringBuilder();
                String readLine;
                while ((readLine = br.readLine()) != null) {
                    responseSb.append(readLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != is) {
                    is.close();
                }
                if (null != br) {
                    br.close();
                }
                urlConnection.disconnect();

            }
            return responseSb.toString();
        } else {
            throw new Exception("error! stateCode " + code);
        }
    }

    /**
     * 初始化请求头
     *
     * @param headers
     * @param urlConnection
     */
    private static void initHeader(Map<String, String> headers, URLConnection urlConnection) {
        urlConnection.setRequestProperty(
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
//        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (headers.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, String>> header = headers.entrySet();
        Iterator<Map.Entry<String, String>> iterator = header.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            urlConnection.setRequestProperty(next.getKey(), next.getValue());
        }
    }

    public static HttpResponse syncGet(String requestUrl, NetConfig netConf, Map<String, String> params,
                                       Map<String, String> headers) throws IOException, Exception {
        String s = urlEncode(params);
        requestUrl += "?" + s;
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(false);//GET方式时需要保持false
//        urlConnection.setUseCaches(false);
//        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(netConf.getConnectionOutTime());
        urlConnection.setReadTimeout(netConf.getReadOutTime());
        initHeader(headers, urlConnection);

        urlConnection.connect();
        int code = urlConnection.getResponseCode();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(code);
        if (code == 200) {
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            BufferedInputStream bis = null;
            try {
                is = urlConnection.getInputStream();
                bis = new BufferedInputStream(is);
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while ((bis.read(buffer)) != -1) {
                    baos.write(buffer);
                }
                baos.flush();
                byte[] body = baos.toByteArray();
                httpResponse = new HttpResponse();
                httpResponse.setBody(body);
            } catch (IOException e) {
                httpResponse.setError(e);
            } finally {
                if (null != baos) {
                    baos.close();
                }
//                if (null != is) {
//                    is.close();
//                }
                if (null != bis) {
                    bis.close();
                }
                urlConnection.disconnect();
            }
        } else {
            httpResponse.setResponseCode(500);
            Exception exception = new Exception("error! stateCode " + code);
            httpResponse.setError(exception);
            throw exception;
        }
        return httpResponse;
    }

    public static<Q> HttpResponse<Q> syncBufferGet(String requestUrl, NetConfig netConf, Map<String, String> params,
                                                     Map<String, String> headers) throws IOException, Exception {
        if (params != null && !params.isEmpty()) {
            String s = urlEncode(params);
            requestUrl += "?" + s;
        }
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(false);//GET方式时需要保持false
//        urlConnection.setUseCaches(false);
//        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setRequestMethod("GET");
        if (netConf == null) {
            netConf = new NetConfig();
        }
        urlConnection.setConnectTimeout(netConf.getConnectionOutTime());
        urlConnection.setReadTimeout(netConf.getReadOutTime());
        if (headers != null && !headers.isEmpty()) {
            initHeader(headers, urlConnection);
        }

        urlConnection.connect();
        int code = urlConnection.getResponseCode();
        HttpResponse<Q> httpResponse = new HttpResponse<>();
        httpResponse.setResponseCode(code);
        if (code == 200) {
            InputStream is;
            Buffer buf = new Buffer();
            try {
                is = urlConnection.getInputStream();
                buf.readFrom(is);
                httpResponse.setBuffer(buf);
            } catch (IOException e) {
                throw e;
            } finally {
                urlConnection.disconnect();
            }
        } else {
            httpResponse.setResponseCode(500);
            Exception exception = new Exception("error! stateCode " + code);
            httpResponse.setError(exception);
            throw exception;
        }
        return httpResponse;

    }

    /**
     * @param requestUrl
     * @param netConf
     * @param params
     * @param headers    @return
     * @throws IOException
     */
    public static String doPost(String requestUrl, NetConfig netConf, Map<String, String> params, Map<String, String> headers) throws IOException, Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(false);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.setConnectTimeout(netConf.getConnectionOutTime());
        urlConnection.setReadTimeout(netConf.getReadOutTime());
        initHeader(headers, urlConnection);
        urlConnection.connect();

        DataOutputStream out = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            out = new DataOutputStream(urlConnection.getOutputStream());
            String content = urlEncode(params);
            out.writeBytes(content);
            out.flush();

            int code = urlConnection.getResponseCode();
            if (code == 200) {
                is = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                StringBuilder responseSb = new StringBuilder();
                String readLine;
                while ((readLine = br.readLine()) != null) {
                    responseSb.append(readLine);
                }
                urlConnection.disconnect();
                return responseSb.toString();
            } else {
                throw new Exception("error! stateCode " + code);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }

    public static HttpResponse syncPost(String requestUrl, NetConfig netConf, Map<String, String> params,
                                        Map<String, String> headers) throws IOException, Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(false);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.setConnectTimeout(netConf.getConnectionOutTime());
        urlConnection.setReadTimeout(netConf.getReadOutTime());
        initHeader(headers, urlConnection);
        urlConnection.connect();

        DataOutputStream out = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        BufferedInputStream bis = null;
        HttpResponse httpResponse = new HttpResponse();
        try {
            out = new DataOutputStream(urlConnection.getOutputStream());
            String content = urlEncode(params);
            out.writeBytes(content);
            out.flush();

            int code = urlConnection.getResponseCode();
            httpResponse.setResponseCode(code);
            if (code == 200) {
                is = urlConnection.getInputStream();
                bis = new BufferedInputStream(is);
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while ((bis.read(buffer)) != -1) {
                    baos.write(buffer);
                }
                baos.flush();
                byte[] body = baos.toByteArray();
                httpResponse = new HttpResponse();
                httpResponse.setBody(body);
                urlConnection.disconnect();
            } else {
                Exception e = new Exception("error! stateCode " + code);
                httpResponse.setResponseCode(500);
                httpResponse.setError(e);
                throw e;
            }
        } catch (IOException e) {
            httpResponse.setError(e);
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            if (baos != null) {
                baos.close();
            }
//            if (is != null) {
//                is.close();
//            }
            if (bis != null) {
                bis.close();
            }
        }
        return httpResponse;
    }

    public static<Q> HttpResponse<Q> syncBufferPost(String requestUrl, NetConfig netConf, Map<String, String> params,
                                                      Map<String, String> headers) throws IOException, Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(false);
        urlConnection.setInstanceFollowRedirects(false);
        if (netConf == null) {
            netConf = new NetConfig();
        }
        urlConnection.setConnectTimeout(netConf.getConnectionOutTime());
        urlConnection.setReadTimeout(netConf.getReadOutTime());
        if (headers != null && !headers.isEmpty()) {
            initHeader(headers, urlConnection);
        }
        urlConnection.connect();

        DataOutputStream out = null;
        InputStream is;
        HttpResponse<Q> httpResponse = new HttpResponse<>();
        try {
            out = new DataOutputStream(urlConnection.getOutputStream());
            String content = urlEncode(params);
            out.writeBytes(content);
            out.flush();

            int code = urlConnection.getResponseCode();
            httpResponse.setResponseCode(code);
            if (code == 200) {
                is = urlConnection.getInputStream();
                httpResponse.setBuffer(new Buffer().readFrom(is));
                urlConnection.disconnect();
            } else {
                Exception e = new Exception("error! stateCode " + code);
                httpResponse.setResponseCode(500);
                httpResponse.setError(e);
                throw e;
            }
        } catch (IOException e) {
            httpResponse.setError(e);
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return httpResponse;
    }

    public static String urlEncode(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        try {
            int size = data.entrySet().size();
            int cishu = 0;
            int h = size - 1;
            for (Map.Entry<String, String> i : data.entrySet()) {
                sb.append(i.getKey())
                        .append("=")
                        .append(URLEncoder.encode(i.getValue(), "UTF-8"));
                if ((cishu++) < h) {
                    sb.append("&");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
