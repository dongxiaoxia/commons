package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.AbstractDataSource;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.DataSourceFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.ConfigUtil;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.DataSourceConfig;

import javax.sql.DataSource;

/**
 * 根据配置文件，得到对应的ConnectionPool，该ConnectionPool封装SWAP DataSource实现
 *
 * @author dongxiaoxia
 * @create 2016-07-12 12:12
 */
public class ConnectionPoolFactory {

    private ConnectionPoolFactory(){

    }

    public synchronized static ConnectionPool createPool(String configPath) throws Exception {
        DataSourceConfig dataSourceConfig = ConfigUtil.getDataSourceConfig(configPath);
        DataSourceFactory.setConfig(dataSourceConfig);
        DataSource dataSource = DataSourceFactory.getDataSource("_DEFAULTD");
        AbstractDataSource abstractDataSource = (AbstractDataSource) dataSource;
        SwapConnectionPool dbConnectionPool = new SwapConnectionPool(abstractDataSource);
        return dbConnectionPool;
    }
}
