package xyz.dongxiaoxia.commons.utils;

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
}
