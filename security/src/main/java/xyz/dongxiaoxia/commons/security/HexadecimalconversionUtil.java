package xyz.dongxiaoxia.commons.security;

/**
 * 进制转换工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-14 22:32
 */
public class HexadecimalconversionUtil {

    private HexadecimalconversionUtil() {
    }

    public static void main(String[] args) {
        String src = "www.dongxiaoxia.xyz";
        System.out.println(parseByteArray2HexStr(src.getBytes()));
        System.out.println(parseByteArray2HexStr2(src.getBytes()));
        System.out.println(parseByteArray2HexStr3(src.getBytes()));
        System.out.println(parseByteArray2HexStr4(src.getBytes()));
        System.out.println(Base64Util.encode(parseHexStr2ByteArray("123")));
    }

    /**
     * 二进制字节数组转换为16进制字符串
     *
     * @param data
     * @return
     */
    public static String parseByteArray2HexStr(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int h = data[i] & 0XFF;
            if (h < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(h));
        }

        return hex.toString();
    }

    /**
     * 写法2
     * <p>
     * 将字节数组转换为16进制字符串
     *
     * @param byteArr
     * @return 16进制字符串
     */
    private static String parseByteArray2HexStr2(byte[] byteArr) {
        // Initialize the character array, used to store each hexadecimal string
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // Initialize a char Array, used to form the result string
        char[] resultCharArr = new char[byteArr.length * 2];
        // Traverse the byte array, converted into characters in a character array
        int index = 0;
        for (byte b : byteArr) {
            resultCharArr[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArr[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArr);
    }

    /**
     * 写法3
     * <p>
     * 将字节数组转换为16进制字符串
     *
     * @param data
     * @return 16进制字符串
     */
    private static String parseByteArray2HexStr3(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : data) {
            if (Integer.toHexString(0xFF & b).length() == 1) {
                buffer.append("0").append(Integer.toHexString(0xFF & b));
            } else {
                buffer.append(Integer.toHexString(0xFF & b));
            }
        }
        return buffer.toString();
    }

    /**
     * 写法4
     * <p>
     * 将字节数组转换为16进制字符串
     *
     * @param data
     * @return 16进制字符串
     */
    private static String parseByteArray2HexStr4(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : data) {
            if (b < 0) {
                b += 256;
            }
            if (b<16){
                buffer.append("0");
            }
            buffer.append(Integer.toHexString(b));
        }
        return buffer.toString();
    }

    /**
     * 16进制字符串转换为2进制字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] parseHexStr2ByteArray(String hex) {
        if (hex == null || "".equals(hex)) {
            return null;
        }

        int length = hex.length() >> 1;
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            int n = i << 1;
            int height = Integer.valueOf(hex.substring(n, n + 1), 16);
            int low = Integer.valueOf(hex.substring(n + 1, n + 2), 16);
            data[i] = (byte) (height * 16 + low);
        }
        return data;
    }
}
