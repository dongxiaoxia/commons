package xyz.dongxiaoxia.commons.security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Base64编码解码工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-14 20:01
 */
public class Base64Util {

    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

    private Base64Util() {
    }

    /**
     * 编码
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
        return encoder.encode(data);
    }

    /**
     * 解码
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] decode(String data) throws IOException {
        return decoder.decodeBuffer(data);
    }

}
