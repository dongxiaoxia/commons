package xyz.dongxiaoxia.commons.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by ucs_chenwendong on 2017/6/21.
 */
public class HttpURLConnectionUtils {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final int TIMEOUT = 8000;

    public static String get(String url) throws Exception {
        return request(url, null, false, GET);
    }

    /**
     * 发起get请求
     *
     * @param url    请求URL
     * @param params 键值对形式的参数
     * @return 请求结果
     * @throws Exception http请求过程可能跑错异常
     */
    public static String get(String url, String params) throws Exception {
        if (url.contains("?")) {
            url = url.concat(params);
        } else {
            url = url.concat("?").concat(params);
        }
        return request(url, null, false, GET);
    }

    /**
     * 发起get请求
     *
     * @param url    请求URL
     * @param params 集合形式的参数
     * @return 请求结果
     * @throws Exception http请求过程可能跑错异常
     */
    public static String get(String url, Map<String, Object> params) throws Exception {
        if (url.contains("?")) {
            url = url.concat(toKeyValueStr(params));
        } else {
            url = url.concat("?").concat(toKeyValueStr(params));
        }
        return request(url, null, false, GET);
    }

    /**
     * 发起post请求
     *
     * @param url 请求URL
     * @return 请求结果
     * @throws Exception http请求过程可能跑错异常
     */
    public static String post(String url) throws Exception {
        return request(url, null, false, POST);
    }

    /**
     * 与键值对形式发起post请求
     *
     * @param url  请求URL
     * @param data 键值对形式请求数据
     * @return 请求结果
     * @throws Exception http请求过程可能跑错异常
     */
    public static String post(String url, String data) throws Exception {
        return request(url, data, false, POST);
    }

    /**
     * 与键值对形式发起post请求
     *
     * @param url  请求URL
     * @param data 集合形式的请求数据
     * @return 请求结果
     * @throws Exception http请求过程可能跑错异常
     */
    public static String post(String url, Map<String, Object> data) throws Exception {
        return request(url, toKeyValueStr(data), false, POST);
    }

    /**
     * 以json形式发起post请求
     *
     * @param url  请求URL
     * @param json json形式字符串
     * @return 请求结果
     * @throws Exception http请求过程可能跑错异常
     */
    public static String postJson(String url, String json) throws Exception {
        return request(url, json, true, POST);
    }

    /**
     * 发起http请求
     *
     * @param urlStr      URL
     * @param data        发送的数据
     * @param isJson      是否json格式
     * @param requestType 请求方式（get 或 post）
     * @return 请求结果字符串
     * @throws Exception http请求过程可能跑错异常
     */
    private static String request(String urlStr, String data, boolean isJson, String requestType) throws Exception {
        HttpURLConnection connection = null;
        try {
            // 调用URL对象的openConnection方法获取HttpURLConnection的实例
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            if (isJson) {
                connection.setRequestProperty("Content-Type", "application/json");
            }
            // 设置请求方式，GET或POST
            connection.setRequestMethod(requestType);
            if (data != null) {
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(data);
            }
            // 设置连接超时、读取超时的时间，单位为毫秒（ms）
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            // getInputStream方法获取服务器返回的输入流
            InputStream in = connection.getInputStream();
            // 使用BufferedReader对象读取返回的数据流
            // 按行读取，存储在StringBuider对象response中
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            if (connection != null) {
                // 结束后，关闭连接
                connection.disconnect();
            }
        }
    }

    /**
     * 参数集合转换为键值对参数字符串
     *
     * @param data 参数集合
     * @return 键值对参数字符串
     */
    private static String toKeyValueStr(Map<String, Object> data) {
        if (data != null && !data.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            data.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
            String keyValueStr = builder.toString();
            if (keyValueStr.charAt(keyValueStr.length() - 1) == '&') {
                keyValueStr = keyValueStr.substring(0, keyValueStr.length() - 1);
            }
            return keyValueStr;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"levelCodes\":[\"0006\",\"0004\",\"0005\"],\"account\":\"3759034\",\"appId\":\"APP_21344\"}";
        String url = "http://localhost:8080/enterpriseInfo";
        System.out.println(postJson(url, json));

    }
}
