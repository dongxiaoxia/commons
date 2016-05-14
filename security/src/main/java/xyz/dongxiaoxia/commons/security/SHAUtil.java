package xyz.dongxiaoxia.commons.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA加密工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-14 20:15
 */
public class SHAUtil {

    private static final String SHA = "SHA";
    private static final String UTF8 = "UTF-8";

    private SHAUtil() {
    }

    /**
     * 获取文件的SHA
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static String getFileSHA(String fileUrl) throws IOException {
        int bufferSize = 1024 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            MessageDigest mDigest = MessageDigest.getInstance(SHA);
            fileInputStream = new FileInputStream(fileUrl);
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
     * SHA加密
     *
     * @param message 要进行SHA加密的字符串
     * @return 返回SHA加密后的十六进制字符串
     */
    public static String getSHA(String message) throws Exception {
        return HexadecimalconversionUtil.parseByteArray2HexStr(getSHAByteArray(message.getBytes(UTF8)));
    }

    /**
     * 获取SHA加密后的二进制字节数组
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] getSHAByteArray(byte[] data) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(SHA);
        messageDigest.reset();
        messageDigest.update(data);
        return messageDigest.digest();
    }
}
