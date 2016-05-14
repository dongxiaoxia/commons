package xyz.dongxiaoxia.commons.logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author dongxiaoxia
 * @create 2016-05-13 23:42
 */
public class PropertiesHelper {
    private Properties properties = null;

    public PropertiesHelper(String fullFilePath) throws IOException {
        properties = loadProperty(fullFilePath);
    }

    public PropertiesHelper(InputStream inputStream) {
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties loadProperty(String filePath) throws IOException {
        FileInputStream fileInputStream = null;
        Properties properties = new Properties();
        try {
            fileInputStream = new FileInputStream(filePath);
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw e;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return properties;
    }

    public String getString(String key) throws Exception {
        try {
            return properties.getProperty(key);
        }catch (Exception e){
            throw new Exception("key:" + key);
        }
    }

    public int getInt(String key) throws Exception {
        try {
            return Integer.parseInt(properties.getProperty(key));
        }catch (Exception e){
            throw new Exception("key:"+key);
        }
    }

    public double getDouble(String key) throws Exception {
        try {
            return Double.parseDouble(properties.getProperty(key));
        }catch (Exception e){
            throw new Exception("key:" + key);
        }
    }

    public long getLong(String key) throws Exception {
        try {
            return Long.parseLong(properties.getProperty(key));
        } catch (Exception e) {
            throw new Exception("key:" + key);
        }
    }

    public float getFloat(String key) throws Exception {
        try {
            return Float.parseFloat(properties.getProperty(key));
        } catch (Exception e) {
            throw new Exception("key:" + key);
        }
    }

    public boolean getBoolean(String key) throws Exception {
        try {
            return Boolean.parseBoolean(properties.getProperty(key));
        } catch (Exception e) {
            throw new Exception("key:" + key);
        }
    }

    public Set<Object> getAllKey(){
        return properties.keySet();
    }

    public Collection<Object> getAllValues(){
        return properties.values();
    }

    public Map<String,Object> getAllKeyValue(){
        Map<String,Object> mapAll = new HashMap<>();
        Set<Object> keys = getAllKey();
        Iterator<Object> it = keys.iterator();
        while (it.hasNext()){
            String key = it.next().toString();
            mapAll.put(key,properties.get(key));
        }
        return mapAll;
    }
}
