package xyz.dongxiaoxia.commons.utils.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author dongxiaoxia
 * @create 2016-07-01 22:58
 */
public class Global {

    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    /**
     * 保存全局属性值
     */
    private static Map<String,String> map = new HashMap<>();

    /**
     * 属性文件加载
     */
    private static PropertiesLoader loader = new PropertiesLoader("common.properties","common2.properties");

    /**
     * 显示/隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 获取当前对象实例
     *
     * @return
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     * @param key
     * @return
     */
    public static String getConfig(String key){
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key,value!=null? value : "");
        }
        return value;
    }


}
