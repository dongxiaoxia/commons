package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.ClusterConfig;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.DataSourceConfig;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSource的类工厂
 *
 * @author dongxiaoxia
 * @create 2016-07-12 14:39
 */
public class DataSourceFactory {

    /**
     * 系统当前所有的数据库池群
     */
    private static Map<String,AbstractDataSource> clouds = new HashMap<>();

    private DataSourceFactory(){

    }

    public static DataSource getDataSource(String dataSourceName) throws Exception {
        return getAbstractDataSource(dataSourceName);
    }

    public static AbstractDataSource getAbstractDataSource(String dataSourceName) throws Exception {
        AbstractDataSource dataSource = clouds.get(dataSourceName);
        if (dataSource !=null){
            return dataSource;
        }else{
            throw new Exception("there is no dataSourceConfig for " + dataSourceName);
        }
    }

    public synchronized static void setConfig(DataSourceConfig config) throws Exception {
        if (clouds.size() != 0){
            clouds.clear();
        }
        Map<String,ClusterConfig> clusters = config.getDataSourceConfig();
        for (String key : clusters.keySet()){
            String clazzName = clusters.get(key).getDataSource();
            Object obj = Class.forName(clazzName).getConstructor(ClusterConfig.class).newInstance(clusters.get(key));
            if (obj instanceof  AbstractDataSource){
                clouds.put(key, (AbstractDataSource) obj);
            }
        }
    }
}
