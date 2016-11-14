package xyz.dongxiaoxia.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 01don on 2016/11/14.
 */
public class JackSonParseTool {
    private static final ObjectMapper MAPPER = JsonParserMapperTool.getMapper();

    /**
     * 将字符串转换为valueType类型的Java对象
     *
     * @param jsonStr
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse2java(String jsonStr, Class<T> valueType) throws IOException {
        return MAPPER.readValue(jsonStr, valueType);
    }

    /**
     * 将字符串转换为HashMap类型的对象
     *
     * @param jsonStr
     * @param class1
     * @param class2
     * @param <T1>
     * @param <T2>
     * @return
     * @throws IOException
     */
    public static <T1, T2> Map<T1, T2> parse2HashMap(String jsonStr, Class<T1> class1, Class<T2> class2) throws IOException {
        return MAPPER.readValue(jsonStr, new TypeReference<HashMap<T1, T2>>() {
        });
    }

    /**
     * 将输入流转换为valueType类型的对象
     *
     * @param inputStream
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse2java(InputStream inputStream, Class<T> valueType) throws IOException {
        return MAPPER.readValue(inputStream, valueType);
    }

    /**
     * 将字符流转换为valueType类型的对象
     *
     * @param bytes
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse2java(byte[] bytes, Class<T> valueType) throws IOException {
        return MAPPER.readValue(bytes, valueType);
    }

    /**
     * 将对象序列化为字符串
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String parse2Json(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    /**
     * 将对象序列化并输出
     *
     * @param out
     * @param object
     * @throws IOException
     */
    public static void parse2OutputStream(final OutputStream out, Object object) throws IOException {
        MAPPER.writeValue(out, object);
    }

    /**
     * 将对象序列化为字节数组
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static byte[] parse2ByteArray(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsBytes(object);
    }




}
