package xyz.dongxiaoxia.commons.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 01don on 2016/11/15.
 */
public class HttpUtils {
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();//// TODO: 2016/11/15 建造者模式？

    private HttpUtils() {

    }

    /**
     * 发送post请求
     *
     * @param url 请求地址
     * @return
     */
    public static String sentHttpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送post请求
     *
     * @param url    请求地址
     * @param params 参数(格式：key1=value1&key2=value2)
     * @return
     */
    public static String sendHttpPost(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(params, "UTF-8");
        entity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(entity);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String sendHttpPost(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        //创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送post请求（带文件）
     * @param url
     * @param params
     * @param fileList
     * @return
     */
    public static String sendHttpPost(String url, Map<String, String> params, List<File> fileList) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(key, new StringBody(params.get(key), ContentType.TEXT_PLAIN));
            }
        }
        if (fileList!=null && !fileList.isEmpty()){
            for (File file :fileList){
                builder.addPart("files",new FileBody(file));
            }
        }
        httpPost.setEntity(builder.build());
        return sendHttpPost(httpPost);
    }

    /**
     * 发送post请求
     * @param httpPost
     * @return
     */
    private static String sendHttpPost(HttpPost httpPost){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            //创建默认的httpclient实例
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            //执行请求
            httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            responseContent = EntityUtils.toString(entity,"UTF--8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (httpResponse!= null){
                    httpResponse.close();
                }
                if (httpClient!=null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }
}
