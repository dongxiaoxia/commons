package xyz.dongxiaoxia.commons.utils;

import java.util.regex.Pattern;

/**
 * Created by dongxiaoxia on 2016/11/14.
 * <p>
 * 正则工具类
 */
public class RegexUtils {

    /**
     * 正则表达式匹配
     *
     * @param text  待匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean checkMatch(String text, String regex) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(regex)) {
            return false;
        }
        return Pattern.compile(regex).matcher(text).matches();
    }
}
