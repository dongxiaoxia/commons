package xyz.dongxiaoxia.commons.persistence.uya.jdbc.util;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 15:34
 */
public class OutSQL {
    private String sql;
    private String realSql;

    public OutSQL(){

    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getRealSql() {
        return realSql;
    }

    public void setRealSql(String realSql) {
        this.realSql = realSql;
    }
}
