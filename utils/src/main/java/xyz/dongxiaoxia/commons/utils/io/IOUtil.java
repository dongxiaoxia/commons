package xyz.dongxiaoxia.commons.utils.io;


import java.io.*;

/**
 * Created by dongxiaoxia on 2017/6/22.
 * IO工具类
 */
public class IOUtil {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * 关闭资源
     *
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    /**
     * InputStream 转换为 String
     *
     * @param input 输入流
     * @return 输入流转换的字符串
     * @throws IOException          IO异常
     * @throws NullPointerException 参数为空时
     */
    public static String toString(InputStream input) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[DEFAULT_BUFFER_SIZE];
        for (int n; (n = input.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        IOUtil.closeQuietly(input);
        return out.toString();
    }

    /**
     * 输入流转换为文件
     *
     * @param input 输入流
     * @param file  要输出的文件
     * @throws IOException IO异常
     */
    public static void toFile(InputStream input, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int byteRead;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while ((byteRead = input.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
            os.write(buffer, 0, byteRead);
        }
        IOUtil.closeQuietly(os);
        IOUtil.closeQuietly(input);
    }

    public static void main(String[] args) throws IOException {
        InputStream input = new BufferedInputStream(new ByteArrayInputStream("hahahah东小侠".getBytes()));
        IOUtil.toFile(input, new File("D:\\haha.txt"));
        input = new BufferedInputStream(new ByteArrayInputStream("hahahah东小侠".getBytes()));
        System.out.println(IOUtil.toString(input));
    }
}
