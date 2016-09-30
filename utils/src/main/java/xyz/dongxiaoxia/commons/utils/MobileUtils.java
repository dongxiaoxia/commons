package xyz.dongxiaoxia.commons.utils;

import java.util.regex.Pattern;

/**
 * Created by dongxiaoxia on 2016/9/30.
 * 手机工具类
 */
public class MobileUtils {
    /**
     * 手机正则表达式
     */
    public static final String PATTERN = "^(\\+86 ?)?1((3)|(4)|(5)|(7)|(8))[0-9]((-)|( ))?[0-9]{4}((-)|( ))?[0-9]{4}$";

    /**
     * 验证手机是否符合正则规律
     * 手机号码规则举例
     * 15089812448
     * 170-8981-2448
     * 170 8981 2448
     * +86 170 8981 2448
     *
     * @param mobile 手机号码
     * @return 是否符合正则表达式
     */
    public static boolean matches(String mobile) {
        return Pattern.matches(PATTERN, mobile);
    }

    /**
     * 格式化手机号码
     *
     * @param mobile
     * @return
     */
    public static String format(String mobile) {
        return mobile.replaceAll("-", "").replaceAll("\\+86", "").replaceAll(" ", "");
    }

    /**
     * 展示手机号码
     *
     * @param mobile
     * @return
     */
    public static String show(String mobile) {
        mobile = mobile.replaceAll("-", "").replaceAll("\\+86", "").replaceAll(" ", "");
        return mobile.substring(0,3)+ "-" + mobile.substring(3,7) + "-" + mobile.substring(7,11);
    }
}
