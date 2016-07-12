package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPool;

/**
 * 辅助类 用来引出 sql 和 proc
 *
 * @author dongxiaoxia
 * @create 2016-07-11 14:15
 */
public class DAOHelper {
    /**
     * exec sql handle
     */
    public IDAO sql = null;

    /**
     * exec proc handle
     */
    public IDAO proc = null;

    private ConnectionHelper connHelper;

    public DAOHelper(ConnectionHelper connHelper){
        this.connHelper = connHelper;
    }

    @Deprecated
    public ConnectionPool getConnPool(){
        return connHelper.getConnPool();
    }
}
