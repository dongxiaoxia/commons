package xyz.dongxiaoxia.commons.utils;

import org.junit.Test;

import java.util.ResourceBundle;

/**
 * Created by dongxiaoxia on 2016/10/13.
 */
public class PropertiesLoaderTest {
    @Test
    public void test() {
        PropertiesLoader loader = new PropertiesLoader("common");
        System.out.println(loader.getAllValues());

        PropertiesLoader loader1 = new PropertiesLoader("config/common.properties");
        System.out.println(loader1.getAllValues());

        PropertiesLoader loader2 = new PropertiesLoader("common.properties","common2.properties");
        System.out.println(loader2.getAllValues());
    }

    @Test
    public void test2() {
        //配置文件是相对ClassPath而言的，并且是.Properties,传参数时只传文件名称，不带后缀名
        ResourceBundle bundle = ResourceBundle.getBundle("common");
        System.out.println(bundle.getString("abc"));
    }
}