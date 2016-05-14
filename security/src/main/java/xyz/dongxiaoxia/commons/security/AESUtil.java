package xyz.dongxiaoxia.commons.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加解密工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-14 21:42
 */
public class AESUtil {
    private static final String AES = "AES";
    private static final int KEY_SIZE = 128;

    private AESUtil() {
    }

    /**
     * AES加密
     * @param data 待加密字符串
     * @param secretKey 用于生成密钥的基础字符串
     * @return 加密字节数组
     * @throws Exception
     */
    public static byte[] encrypt(String data, String secretKey) throws Exception {
        if(secretKey == null || "".equals(secretKey))
        {
            throw new Exception("secretKey need to exists");
        }
        Cipher cipher = Cipher.getInstance(AES);// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, initKey(secretKey));// 初始化
        return cipher.doFinal(data.getBytes("UTF-8"));
    }

    /**
     *  AES解密
     * @param data 待解密字节数组
     * @param secretKey 用于生成密钥的基础字符串,需要注意的是EAS是对称加密，所以secretKeyBase在加密解密时要一样的
     * @return 解密后字符串
     * @throws Exception
     */
    public static String decrypt(byte[] data, String secretKey) throws Exception {
        if(secretKey == null || "".equals(secretKey))
        {
            throw new Exception("secretKey need to exists");
        }
        Cipher cipher = Cipher.getInstance(AES);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, initKey(secretKey));// 初始化
       return new String(cipher.doFinal(data), "UTF-8");
    }

    /**
     * 初始化密钥
     *
     * @return
     */
    private static SecretKeySpec initKey(String secretKey) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = new SecureRandom(secretKey.getBytes());
        keyGenerator.init(KEY_SIZE, secureRandom);
        return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), AES);
    }
}
