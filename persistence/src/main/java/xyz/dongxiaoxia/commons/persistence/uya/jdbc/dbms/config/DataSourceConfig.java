package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有数据源的配置文件
 *
 * @author dongxiaoxia
 * @create 2016-07-12 12:49
 */
public class DataSourceConfig {

    //系统当前的所有数据源，主键为数据源的服务名称，值为该服务所包含的参数数据源对象集合。
    private Map<String,ClusterConfig> clusterConfigMap = new HashMap<>();

    public void addClusterConfig(ClusterConfig clusterConfig){
        if (clusterConfig != null){
            clusterConfigMap.put(clusterConfig.getName(),clusterConfig);
        }
    }

    public ClusterConfig getDataSourceConfig(String datasourceName){
        return clusterConfigMap.get(datasourceName);
    }

    public Map<String,ClusterConfig> getDataSourceConfig(){
        return clusterConfigMap;
    }
}
