package xyz.dongxiaoxia.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dongxiaoxia on 2016/11/14.
 */
public class StringUtils {


    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    static Pattern formatPattern = Pattern.compile("\\{(\\d)}");

    /**
     * 字符串格式化，以{index}为占位符
     *
     * @param format  {0}abc{1}
     * @param arr
     * @return
     */
    public static String format(String format, Object... arr) {
        if (isEmpty(format)) {
            return format;
        }
        Matcher m = formatPattern.matcher(format);
        while (m.find()) {
            Object f = arr[Integer.parseInt(m.group(1))];
            format = format.replace(m.group(), f == null ? "" : f.toString());
        }
        return format;
    }
}
