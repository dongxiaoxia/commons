package xyz.dongxiaoxia.commons.utils;

import org.junit.Test;

import java.util.Properties;

/**
 * Created by dongxiaoxia on 2016/10/13.
 */
public class PropertiesLoaderTest {
    @Test
    public void test(){
        Properties properties = PropertiesLoader.getProperties("common.properties");
        System.out.println(properties.getProperty("abc"));

        PropertiesLoader propertiesLoader = new PropertiesLoader("common.properties");
        System.out.println(propertiesLoader.getAllValues());
    }

}