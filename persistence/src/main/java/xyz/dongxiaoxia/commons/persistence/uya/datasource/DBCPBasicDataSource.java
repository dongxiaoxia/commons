package xyz.dongxiaoxia.commons.persistence.uya.datasource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 自定义DBCP数据源，实现配置文件密码加密
 *
 * @author dongxiaoxia
 * @create 2017-12-27 20:49:57
 */
public class DBCPBasicDataSource extends BasicDataSource {

    public void setPassword(String password) {
        try {
            super.setPassword(DataSourceSecurity.decrypt(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(DataSourceSecurity.encrypt("1234"));
    }
}

