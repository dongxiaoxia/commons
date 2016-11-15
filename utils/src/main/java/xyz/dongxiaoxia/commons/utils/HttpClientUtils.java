package xyz.dongxiaoxia.commons.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import xyz.dongxiaoxia.json.JackSonParseTool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by 01don on 2016/11/15.
 */
public class HttpClientUtils {
    public static <T> T sendGet(String url, Class<T> clazz) {
        try {
            CloseableHttpResponse response = sendGet(url, null, null);
            if (response == null) {
                return null;
            } else {
                return JackSonParseTool.parse2java(response.getEntity().getContent(), clazz);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CloseableHttpResponse sendGet(String url, Map<String, String> headers, Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null && !params.isEmpty()) {
            char a = '&';
            char b = '=';
            char c = '?';

            StringBuilder queryString = new StringBuilder();
            if (url.indexOf(c) < 0) {
                queryString.append(c);
            } else if (!url.startsWith(a + "")) {
                queryString.append(a);
            }
            Set<String> keys = params.keySet();
            for (String key : keys) {
                queryString.append(key).append(b).append(URLEncoder.encode(params.get(key), "UTF-8")).append(a);
            }
            url += queryString;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpGet.addHeader(key, headers.get(key));
            }
        }
        try {
            return httpClient.execute(httpGet);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CloseableHttpResponse sendMultipartPost(String url, Map<String, String> headers, HttpEntity httpEntity) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpPost.setHeader(key, headers.get(key));
            }
        }
        if (httpEntity != null) {
            httpPost.setEntity(httpEntity);
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            if (status == null) {
                return null;
            }
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                return response;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T sendPost(String url, Map<String, String> headers, HttpEntity entity, Class<T> clazz) {
        CloseableHttpResponse response = sendPostEntity(url, headers, entity, null);
        if (response == null) {
            return null;
        }
        try {
            return JackSonParseTool.parse2java(response.getEntity().getContent(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CloseableHttpResponse sendPostEntity(String url, Map<String, String> headers, HttpEntity httpEntity, String contentType) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", contentType == null ? "application/json; charset=UTF-8" : contentType);
        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpPost.setHeader(key, headers.get(key));
            }
        }
        if (httpEntity != null) {
            httpPost.setEntity(httpEntity);
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            if (status == null) {
                return null;
            }
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                return response;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
