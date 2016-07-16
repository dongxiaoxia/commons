package xyz.dongxiaoxia.commons.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dongxiaoxia on 2015/11/20.
 * <p>
 * md5工具类
 */
public class Md5Util {

    private static final String MD5 = "MD5";
    private static final String UTF8 = "UTF-8";

    private Md5Util() {
    }

    /**
     * 获取文件的MD5，可以替换为SHA1
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static String getFileMD5(String fileUrl) throws IOException {
        int bufferSize = 1024 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 可以替换为"SHA1"
            MessageDigest mDigest = MessageDigest.getInstance(MD5);
            fileInputStream = new FileInputStream(fileUrl);
            // Creates a digest input stream, using the specified input stream and message digest.
            digestInputStream = new DigestInputStream(fileInputStream, mDigest);
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) ;
            mDigest = digestInputStream.getMessageDigest();
            byte[] resultArr = mDigest.digest();
            return HexadecimalconversionUtil.parseByteArray2HexStr(resultArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (digestInputStream != null) {
                digestInputStream.close();
            }
        }
        return null;
    }


    /**
     * MD5加密
     *
     * @param message 要进行md5加密的字符串
     * @return 加密结果为32位小写字符串
     */
    public static String getMd5(String message) throws Exception {
        return HexadecimalconversionUtil.parseByteArray2HexStr(getMd5ByteArray(message.getBytes(UTF8)));
    }

    /**
     * 获取md5加密后的二进制字节数组
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] getMd5ByteArray(byte[] data) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(MD5);
        messageDigest.reset();
        messageDigest.update(data);
        return messageDigest.digest();
    }
}
