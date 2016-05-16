package xyz.dongxiaoxia.commons.security;

import java.util.Map;

/**
 * 例子
 *
 * @author dongxiaoxia
 * @create 2016-05-14 22:15
 */
public class Example {

    private static final String inputStr = "www.dongxiaoxia.xyz";

    public static void main(String[] args) {
        Example example = new Example();
        example.md5Test();
        example.SHATest();
        example.AESTest();
        example.DESTest();
        example.RSATest();
    }

    public void md5Test() {
        System.out.println("=======================MD5=====================");
        try {
            System.out.println("MD5:" + Md5Util.getMd5(inputStr));
            System.out.println("文件MD5:" + Md5Util.getFileMD5("D:\\GitHub\\commons\\security\\src\\main\\java\\xyz\\dongxiaoxia\\commons\\security\\Example.java"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SHATest() {
        System.out.println("=======================SHA=====================");
        try {
            System.out.println("SHA:" + SHAUtil.getSHA(inputStr));
            System.out.println("文件SHA:" + SHAUtil.getFileSHA("D:\\GitHub\\commons\\security\\src\\main\\java\\xyz\\dongxiaoxia\\commons\\security\\Example.java"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AESTest() {
        System.out.println("=======================AES=====================");
        try {
            System.out.println(inputStr + " 加密后再解码的结果：" + AESUtil.decrypt(AESUtil.encrypt(inputStr, "dongxiaoxia"), "dongxiaoxia"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DESTest() {
        System.out.println("=======================DES=====================");
        try {
            System.out.println(inputStr + " 加密后再解码的结果：" + new String(DESUtil.decrypt(DESUtil.encrypt(inputStr.getBytes(), "dongxiaoxia"), "dongxiaoxia")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RSATest(){
        System.out.println("=======================RSA=====================");
        try {
            Map<String,String> keyMap = RSAUtil.initKey();
            String privateKey = keyMap.get(RSAUtil.PRIVATE_KEY);
            String publicKey = keyMap.get(RSAUtil.PUBLIC_KEY);
            byte[] data = inputStr.getBytes();
            byte[] encryptPrivateData = RSAUtil.encryptPrivate(data,privateKey);
            String sign = RSAUtil.sign(data,privateKey);
            byte[] decryptData = RSAUtil.decryptPublic(encryptPrivateData, publicKey);
            System.out.println(RSAUtil.PRIVATE_KEY+":"+privateKey);
            System.out.println(RSAUtil.PUBLIC_KEY+":"+publicKey);
            System.out.println("in:"+inputStr+",out:"+new String (decryptData)+"verifySign:"+RSAUtil.verifySign(decryptData,publicKey,sign));

            byte[] encryPublicData = RSAUtil.encryptPublic(data, publicKey);
            byte[] decryptPublicData = RSAUtil.decryptPrivate(encryPublicData, privateKey);
            System.out.println("in:"+inputStr+",out:"+new String (decryptPublicData)+"verifySign:");        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
