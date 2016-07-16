package xyz.dongxiaoxia.commons.security;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA非对称加密算法工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-15 1:11
 */
public class RSAUtil {

    private static final String RSA = "RSA";
    //rsa，签名算法可以是 md5withrsa 、 sha1withrsa 、 sha256withrsa 、 sha384withrsa 、 sha512withrsa
    private static final String SIGN = "MD5withRSA";
    private static final int KEY_SIZE =1024;
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    private RSAUtil(){}

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptPrivate(byte[] data,String privateKey) throws Exception{
        PrivateKey key = getRSAPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptPublic(byte[] data, String publicKey) throws Exception {
        if(publicKey == null || "".equals(publicKey))
        {
            throw new Exception("publicKey is need exists");
        }
        PublicKey key = getRSAPublickey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptPrivate(byte[] data,String privateKey) throws Exception{
        PrivateKey key = getRSAPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptPublic(byte[] data,String publicKey) throws Exception{
        PublicKey key = getRSAPublickey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(data);
    }

    /**
     * 获取私钥
     * @param privateKey
     * @return
     * @throws Exception
     */
    private static PrivateKey getRSAPrivateKey(String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 获取公钥
     * @param publicKey
     * @return
     * @throws Exception
     */
    private static PublicKey getRSAPublickey(String publicKey) throws Exception{
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Util.decode(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * 使用私钥对数据进行签名
     * @param data
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data,String privateKeyStr)throws Exception{
        PrivateKey privateKey = getRSAPrivateKey(privateKeyStr);
        Signature signature = Signature.getInstance(SIGN);
        signature.initSign(privateKey);
        signature.update(data);
        return Base64Util.encode(signature.sign());
    }

    /**
     * 使用公钥校验签名
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verifySign(byte[] data, String publicKey, String sign) throws Exception {
        if(publicKey == null || "".equals(publicKey))
        {
            throw new Exception("publicKey is need exists");
        }

        PublicKey rsaPublicKey = getRSAPublickey(publicKey);
        Signature signature = Signature.getInstance(SIGN);
        signature.initVerify(rsaPublicKey);
        signature.update(data);
        return signature.verify(Base64Util.decode(sign));
    }

    /**
     * 生成私钥和公钥
     * @return
     * @throws Exception
     */
    public static Map<String,String> initKey() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Map map = new HashMap(2);
        map.put(PRIVATE_KEY,Base64Util.encode(keyPair.getPrivate().getEncoded()));
        map.put(PUBLIC_KEY, Base64Util.encode(keyPair.getPublic().getEncoded()));
        return map;
    }
}
