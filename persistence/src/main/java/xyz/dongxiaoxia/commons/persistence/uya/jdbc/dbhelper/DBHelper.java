package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao.DAOBase;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao.DAOHelper;

import java.net.URL;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:13
 */
public class DBHelper {

    private static final Logger log = LoggerFactory.getLogger(DBHelper.class);

    private static DAOHelper dbhelper = null;

    private static String configName = "db.properties";

    public static DAOHelper getDbhelper() {
        if (dbhelper != null) {
            return dbhelper;
        }
        synchronized (DBHelper.class) {
            if (dbhelper != null) {
                return dbhelper;
            }
            try {
                URL url = DBHelper.class.getClassLoader().getResource(configName);
                String path =url == null?null: url.getPath();
                log.info("db config path:" + path);
                dbhelper = DAOBase.createInstance(path);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return dbhelper;
    }
}
