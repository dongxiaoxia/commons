package xyz.dongxiaoxia.commons.utils;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author dongxiaoxia
 * @create 2017-12-25 19:48:46
 */
public class CaptchaUtil {
    private static String CheckCode_Session = "captcha";
    private static int WIDTH = 83;
    private static int HEIGHT = 28;
    private static final char[] CHARS = new char[]{'2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};//排除难辨识的字符
    private static Random random = new Random();

    /**
     * @return 随机4位验证码字符串
     */
    public static String getRandomCaptcha() {
        char[] rands = new char[4];
        for (int i = 0; i < 4; ++i) {
            rands[i] = CHARS[random.nextInt(CHARS.length)];
        }
        return new String(rands);
    }

    /**
     * 创建验证码图片
     *
     * @param captcha      验证码字符串
     * @param outputStream 验证码输出流
     */
    private static void createCaptchaImage(String captcha, OutputStream outputStream) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH + 6, HEIGHT + 4, 1);
        Graphics g = image.getGraphics();
        g.setColor(Color.black);
        g.clipRect(0, 0, WIDTH + 6, HEIGHT + 4);
        g.setColor(Color.white);
        g.fillRect(1, 1, WIDTH + 4, HEIGHT + 2);
        Random r = new Random();
        for (int i = 0; i < 30; ++i) {
            int x = r.nextInt(WIDTH) + 2;
            int y = r.nextInt(HEIGHT) + 3;
            int xl = r.nextInt(12);
            int yl = r.nextInt(12);
            int red = r.nextInt(255);
            int green = r.nextInt(255);
            int blue = r.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(x, y, x + xl, y + yl);
        }
        g.setColor(new Color(2039583));
        Font font = new Font("Fixedsys", Font.PLAIN, HEIGHT);
        g.setFont(font);
        char[] rands = captcha.toCharArray();
        int xx = WIDTH / (rands.length + 1);
        int codeY = HEIGHT;
        for (int i = 0; i < rands.length; ++i) {
            g.drawString("" + rands[i], (i + 1) * xx, codeY);
        }
        g.dispose();
        ImageIO.write(image, "JPEG", outputStream);
    }

    /**
     * 创建验证码图片
     *
     * @param outputStream 验证码输出流
     */
    public static void createCaptchaImage(OutputStream outputStream) throws IOException {
        createCaptchaImage(getRandomCaptcha(), outputStream);
    }

    /**
     * 创建验证码图片,在Web应用中使用
     */
    public static void createCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captcha = getRandomCaptcha();
        request.getSession(true).setAttribute(CheckCode_Session, captcha);
        response.setContentType("image/jpeg");
        //禁用缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        createCaptchaImage(captcha, bos);
        byte[] buf = bos.toByteArray();
        response.setContentLength(buf.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(buf);
        bos.close();
        outputStream.flush();
    }

    /**
     * 创建随机颜色风格的验证码图片
     *
     * @param captcha      验证码字符串
     * @param outputStream 验证码输出流
     */
    private static void createCaptchaRandImage(String captcha, OutputStream outputStream) throws IOException {
        Color bcolor = getRandColor(150, 250);
        Color fcolor = getRandColor(30, 100);
        BufferedImage bimage = new BufferedImage(WIDTH, HEIGHT, 4);
        Graphics2D g = bimage.createGraphics();
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.setColor(bcolor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(fcolor);
        g.drawString(captcha, 20, 22);
        int i = 0;
        for (int n = random.nextInt(100); i < n; ++i) {
            g.drawRect(random.nextInt(WIDTH), random.nextInt(HEIGHT), 1, 1);
        }
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
        encoder.encode(bimage);
        outputStream.flush();
    }

    /**
     * 创建随机颜色风格的验证码图片
     *
     * @param outputStream 验证码输出流
     */
    public static void createCaptchaRandImage(OutputStream outputStream) throws IOException {
        createCaptchaRandImage(getRandomCaptcha(), outputStream);
    }

    /**
     * 创建随机颜色风格的验证码图片,在Web应用中使用
     */
    public static void createCaptchaRandImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captcha = getRandomCaptcha();
        request.getSession(true).setAttribute(CheckCode_Session, captcha);
        response.setContentType("image/jpeg");
        //禁用缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        createCaptchaRandImage(captcha, bos);
        byte[] buf = bos.toByteArray();
        response.setContentLength(buf.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(buf);
        bos.close();
        outputStream.flush();
    }

    /**
     * 随机颜色
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            String str = CaptchaUtil.getRandomCaptcha();
            System.out.println(str);
            String fileName = "D:/" + str + ".jpeg";
            FileOutputStream outputStream = new FileOutputStream(fileName);
            CaptchaUtil.createCaptchaImage(outputStream);
            outputStream.close();
        }
        for (int i = 0; i < 10; i++) {
            String str = CaptchaUtil.getRandomCaptcha();
            System.out.println(str);
            String fileName = "D:/" + str + ".jpeg";
            FileOutputStream outputStream = new FileOutputStream(fileName);
            CaptchaUtil.createCaptchaRandImage(new FileOutputStream(fileName));
            outputStream.close();
        }
    }
}

/**
 * Web应用使用demo
 * web.xml
 * <servlet>
 * <servlet-name>captchaServlet</servlet-name>
 * <servlet-class> xyz.dongxiaoxia.commons.utils.CaptchaUtil</servlet-class>
 * </servlet>
 * <p>
 * <servlet-mapping>
 * <servlet-name>captchaServlet</servlet-name>
 * <url-pattern>/s/captcha</url-pattern>
 * </servlet-mapping>
 * <p>
 * servlet类
 * public class CheckCodeServlet extends HttpServlet {
 * public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 * // CaptchaUtil.createCaptchaImage(request, response);
 * CaptchaUtil.createCaptchaRandImage(request, response);
 * }
 * }
 */