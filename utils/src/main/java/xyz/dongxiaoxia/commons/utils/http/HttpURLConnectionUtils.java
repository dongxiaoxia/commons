package xyz.dongxiaoxia.commons.utils.http;

import sun.misc.IOUtils;
import xyz.dongxiaoxia.commons.utils.io.IOUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by dongxiaoxia on 2017/6/21.
 */
public class HttpURLConnectionUtils {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final int TIMEOUT = 8000;

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";

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

    /**
     * 上传文件
     *
     * @param url  文件上传地址
     * @param file 需要上传的文件
     * @return 上传返回结果
     * @throws IOException IO异常
     */
    public static String uploadFile(String url, File file, String params) throws IOException {
        URL urlGet = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(POST);
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", DEFAULT_USER_AGENT);
        conn.setRequestProperty("Charsert", DEFAULT_CHARSET);
        // 定义数据分隔线
        String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL";
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(conn.getOutputStream());
        // 定义最后数据分隔线
        StringBuilder mediaData = new StringBuilder();
        mediaData.append("--").append(BOUNDARY).append("\r\n");
        mediaData.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
        mediaData.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] mediaDatas = mediaData.toString().getBytes();
        out.write(mediaDatas);
        DataInputStream fs = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = fs.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        IOUtil.closeQuietly(fs);
        // 多个文件时，二个文件之间加入这个
        out.write("\r\n".getBytes());
        if (params != null && !params.trim().isEmpty()) {
            StringBuilder paramData = new StringBuilder();
            paramData.append("--").append(BOUNDARY).append("\r\n");
            paramData.append("Content-Disposition: form-data;name=\"description\";");
            byte[] paramDatas = paramData.toString().getBytes();
            out.write(paramDatas);
            out.write(params.getBytes(DEFAULT_CHARSET));
        }
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
        out.write(end_data);
        out.flush();
        IOUtil.closeQuietly(out);

        // 定义BufferedReader输入流来读取URL的响应
        InputStream in = conn.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
        String valueString = null;
        StringBuffer bufferRes = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null) {
            bufferRes.append(valueString);
        }
        IOUtil.closeQuietly(in);
        // 关闭连接
        if (conn != null) {
            conn.disconnect();
        }
        return bufferRes.toString();
    }

    /**
     * 下载文件
     *
     * @param url 文件地址
     * @return InputStream 流，考虑到这里可能返回json或file
     * @throws IOException IO异常
     */
    public static InputStream downloadFile(String url) throws IOException {
        return downloadFile(url, null);
    }

    /**
     * 下载文件
     *
     * @param url 文件地址
     * @return InputStream 流，考虑到这里可能返回json或file
     * @throws IOException IO异常
     */
    public static InputStream downloadFile(String url, String params) throws IOException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        // 连接超时
        conn.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        conn.setReadTimeout(25000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "Keep-Alive");
        conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        if (params != null && !params.isEmpty()) {
            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes(DEFAULT_CHARSET));
            out.flush();
            IOUtil.closeQuietly(out);
        }
        return conn.getInputStream();
    }

    //todo 下载文件
    //todo https

    public static String postSSL(String url, String data, String certPath, String certPass) {
       return null;
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"levelCodes\":[\"0006\",\"0004\",\"0005\"],\"account\":\"3759034\",\"appId\":\"APP_21344\"}";
        String url = "http://localhost:8080/enterpriseInfo";
        System.out.println(postJson(url, json));

    }
}
