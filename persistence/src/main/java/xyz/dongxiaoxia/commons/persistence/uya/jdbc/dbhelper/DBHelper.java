package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao.DAOBase;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao.DAOHelper;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:13
 */
public class DBHelper {

    private static final Logger log = LoggerFactory.getLogger(DBHelper.class);

    private static DAOHelper dbHelper = null;

    static{
        getDbHelper();
    }

    public static DAOHelper getDbHelper() {
        if (dbHelper != null) {
            return dbHelper;
        }
        synchronized (DBHelper.class) {
            if (dbHelper != null) {
                return dbHelper;
            }
            try {
                dbHelper = DAOBase.createInstance();//使用默认的配置名称 db.properties
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return dbHelper;
    }
}
