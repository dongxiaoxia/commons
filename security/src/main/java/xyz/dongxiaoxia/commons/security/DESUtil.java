package xyz.dongxiaoxia.commons.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES对称加密工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-15 0:27
 */
public class DESUtil {
    private static final String DES = "DES";

    private DESUtil(){}

    /**
     * 加密
     * @param data
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,String secretKey) throws Exception{
        if (null == secretKey || "".equals(secretKey) ){
            throw new Exception("secretKey need to exists");
        }
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(secretKey));
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data,String secretKey) throws Exception {
        if (null == secretKey || "".equals(secretKey) ){
            throw new Exception("secretKey need to exists");
        }
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE,getKey(secretKey));
        return cipher.doFinal(data);
    }

    /**
     * 获取对称密钥
     * @param key base64编码后的密钥字符串
     * @return
     * @throws Exception
     */
    private static SecretKey getKey(String key) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(Base64Util.decode(key));
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
        return secretKeyFactory.generateSecret(desKeySpec);
    }
}
