package xyz.dongxiaoxia.commons.utils.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Properties文件载入工具，可载入多个Properties文件，相同属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先。
 *
 * @author dongxiaoxia
 * @create 2016-07-01 23:03
 */
public class PropertiesLoader {

    private static Logger logger = Logger.getLogger(PropertiesLoader.class.getName());

    private final Properties properties;

    public PropertiesLoader(String... resourcesPaths) {
        properties = loadProperties(resourcesPaths);
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
    public Integer getInterger(String key) {
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

    /**
     * 载入多个文件,文件路径使用Spring Resource格式.
     *
     * @param resourcesPaths
     * @return
     */
    private Properties loadProperties(String... resourcesPaths) {
        Properties pros = new Properties();
        for (String location : resourcesPaths) {
            logger.info("Loading properties file from:" + location);
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(PropertiesLoader.class.getClassLoader().getResourceAsStream(location), "UTF-8");
                pros.load(inputStreamReader);
            } catch (Exception e) {
                logger.info("Could not load properties from path:" + location + "," + e.getMessage());
            } finally {
                try {
                    if (inputStreamReader!=null){
                        inputStreamReader.close();
                    }
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return pros;
    }
}
