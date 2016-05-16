package xyz.dongxiaoxia.commons.utils;

import java.io.File;
import java.net.URL;

/**
 * 配置文件工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-13 23:11
 */
public class ConfigFileUtil {
    public static File getConfigFile(String fileName) throws Exception {
        return getConfigFile(null, fileName);
    }

    private static File getConfigFile(String filePath, String fileName) throws Exception {
        try {
            File file = null;
            //指定了filePath,先从filePath + fileName去找
            if (filePath != null && filePath.length() != 0) {
                file = FileUtil.getFile(filePath, fileName);
                if (file != null) {
                    return file;
                }
            }

            ClassLoader classLoader = ConfigFileUtil.class.getClassLoader();
            URL url = classLoader.getResource("META-INF");
            if (url != null) {
                String metaInfPath = url.getPath();
                System.out.println("META-INF PATH : " + metaInfPath);
                if (FileUtil.checkFileExist(metaInfPath, "namespace.properties")) {
                    String namespaceFullPath = metaInfPath + "/namespace.properties";
                    PropertiesHelper propertiesHelper = new PropertiesHelper(namespaceFullPath);
                    String configPath = propertiesHelper.getString("configPath");
                    System.out.println("configPath:" + configPath);
                    if (configPath != null && !configPath.endsWith("\\") && !configPath.endsWith("/")) {
                        configPath += "/";
                    }
                    file = FileUtil.getFile(configPath, fileName);
                    if (file != null) {
                        return file;
                    }
                    System.out.println("propertiesHelper.properties is null:" + namespaceFullPath);
                } else {
                    //在类路径下获取
                    file = FileUtil.getFile(metaInfPath, fileName);
                    if (file != null) {
                        System.out.print("Match metaInfPath:" + file.getPath());
                        return file;
                    }
                }
            }
            String path = classLoader.getResource("").getPath();
            file = FileUtil.getFile(path, fileName);
            if (file != null) {
                System.out.println("Match getResource Path:" + file.getPath());
                return file;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
