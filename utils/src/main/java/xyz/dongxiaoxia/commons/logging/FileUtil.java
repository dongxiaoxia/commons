package xyz.dongxiaoxia.commons.logging;

import java.io.File;

/**
 * 文件工具类
 *
 * @author dongxiaoxia
 * @create 2016-05-13 23:15
 */
public class FileUtil {

    public static File getFile(File file, String fileName) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        File found = getFile(f, fileName);
                        if (found != null) {
                            return found;
                        }
                    }
                }
            }
            return fileName.equalsIgnoreCase(file.getName()) ? file : null;
        }
        return null;
    }

    public static File getFile(String filePath,String fileName){
        File file = getFile(new File(filePath),fileName);
        return getFile(file,fileName);
    }

    public static Boolean checkFileExist(String filePath,String fileName){
        return getFile(new File(filePath), fileName) != null;
    }
}
