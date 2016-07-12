package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源集群的配置文件
 * @author dongxiaoxia
 * @create 2016-07-12 14:03
 */
public class ClusterConfig {

    private String name;
    private String dataSource = "xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.ClusterDataSource";
    private List<DbConfig> dbConfigList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<DbConfig> getDbConfigList() {
        return dbConfigList;
    }

   public void addDbConfig(DbConfig dbConfig){
       if (dbConfig != null){
           this.dbConfigList.add(dbConfig);
       }
   }
}
