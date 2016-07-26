package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao.AbstractDAOHandler;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao.DAOHelper;

import java.net.URL;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:13
 */
public class DBHelper {

    private static final Logger log = LoggerFactory.getLogger(DBHelper.class);

    private static DAOHelper dbHelper = null;

    /**
     * 默认数据库配置文件名称
     */
    private static final String DB_CONFIG_NAME = "db.properties";

    public static DAOHelper getDbHelper() {
        if (dbHelper != null) {
            return dbHelper;
        }
        synchronized (DBHelper.class) {
            if (dbHelper != null) {
                return dbHelper;
            }
            try {
                /**
                 * 默认配置文件在jar包根目录下，可以在项目资源文件根目录下重名覆盖
                 * 创建DAOHelper实例（默认读取jar所在目录下的db.properties文件）
                 */
                URL url = AbstractDAOHandler.class.getClassLoader().getResource(DB_CONFIG_NAME);//使用默认的配置名称 db.properties
                dbHelper = DAOHelper.createDAO(url);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return dbHelper;
    }
}
