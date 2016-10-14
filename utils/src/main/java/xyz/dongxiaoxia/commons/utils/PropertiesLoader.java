package xyz.dongxiaoxia.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;

/**
 * Properties文件载入工具，可载入多个Properties文件，相同属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先。
 *
 * @author dongxiaoxia
 * @create 2016-07-01 23:03
 */
public class PropertiesLoader {

    private static Logger logger = Logger.getLogger(PropertiesLoader.class.getName());

    /**
     * .properties属性文件名后缀
     */
    public static final String PROPERTY_FILE_SUFFIX = ".properties";

    private final Properties properties;

    public PropertiesLoader(String... propsFileNames) {
        properties = loadProperties(propsFileNames);
    }

    public PropertiesLoader(String propsFileName) {
        properties = getProperties(propsFileName);
    }

    public PropertiesLoader(InputStream inputStream) {
        properties = loadProperties(inputStream);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 取出Property，但与System的Property优先，取不到返回空字符串。
     *
     * @param key
     * @return
     */
    private String getValue(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }

    /**
     * 取出String类型的Property，但与System的Property优先，如果都为NUll则抛出异常。
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    /**
     * 取出String类型的Property，但以System的Property优先，如果都为Null则返回Default值。
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 取出Integer类型的Property。但以System的Property优先，如果都为null或内容错误则抛异常。
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先。如果都为null则返回默认值，如果内容错误则抛异常。
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Integer getInteger(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型的Property，但以System的Property优先，如果都为null或内容错误则抛异常。
     *
     * @param key
     * @return
     */
    public Double getDouble(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先，如果都为null则返回默认值，如果内容错误则抛异常。
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Double getDouble(String key, Double defaultValue) {
        String value = getValue(key);
        return value != null ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先，如果都为null则抛出异常，如果内容不是true/false则返回false。
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先，如果都为null则返回默认值，如果内容不为true/false则返回false。
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key);
        return value != null ? Boolean.valueOf(value) : defaultValue;
    }

    public Set<Object> getAllKey() {
        return properties.keySet();
    }

    public Collection<Object> getAllValues() {
        return properties.values();
    }

    public Map<String, Object> getAllKeyValue() {
        Map<String, Object> mapAll = new HashMap<>();
        Set<Object> keys = getAllKey();
        for (Object key1 : keys) {
            String key = key1.toString();
            mapAll.put(key, properties.get(key));
        }
        return mapAll;
    }

    /**
     * 根据Properties配置文件名称获取配置文件对象
     *
     * @param propsFileName Properties配置文件名称（从ClassPath根下获取） 可以不带扩展名
     *                      eg.根目录下有个common.properties,那么可以传“common”或者“common.properties”
     *                      根目录下有个config文件夹，里面存在common.properties,那么可以传“config/common”或者“config/common.properties”
     * @return Properties对象
     */
    private Properties getProperties(String propsFileName) {
        if (propsFileName == null || propsFileName.equals("")) throw new IllegalArgumentException();
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            try {
                if (propsFileName.lastIndexOf(PROPERTY_FILE_SUFFIX) == -1) {//加入文件名后缀
                    propsFileName += PROPERTY_FILE_SUFFIX;
                }
                //写法1：
//                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propsFileName);
                //写法2：
//                URL url = Thread.currentThread().getContextClassLoader().getResource(propsFileName);
//                inputStream = url.openStream();
                //写法3：
                inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(propsFileName);
                //写法4：
//                URL url = PropertyUtil.class.getClassLoader().getResource(propsFileName);
//                inputStream = url.openStream();
                //写法5：
//                inputStream = PropertiesLoader.class.getResourceAsStream("/" + propsFileName);
                if (null != inputStream) properties.load(inputStream);
            } finally {
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
//            LOGGER.error("加载属性文件出错!", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
        return properties;
    }

    /**
     * 载入多个文件
     *
     * @param propsFileNames
     * @return
     */
    private Properties loadProperties(String... propsFileNames) {
        Properties pros = new Properties();
        for (String propsFileName : propsFileNames) {
            InputStream inputStream = null;
            try {
                inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(propsFileName);
                pros.load(inputStream);
            } catch (Exception e) {
                logger.info("Could not load properties from path:" + propsFileName + "," + e.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return pros;
    }

    /**
     * 根据输入流载入Properties对象
     *
     * @param inputStream
     * @return
     */
    private Properties loadProperties(InputStream inputStream) {
        Properties pros = new Properties();
//        pros.load(inputStream);
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
            pros.load(inputStreamReader);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return pros;
    }
}
