package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.ClusterConfig;

import java.sql.SQLException;

public class ReadAndWriteDataSource extends ClusterDataSource {
	
	/**
	 * DataSourceCloud 的构造函数 根据DataSourceConfig 进行初始化操作 可同时初始化 多个源文件。
	 * 
	 * @param dataSourceConfig
	 * @throws Exception
	 */
	public ReadAndWriteDataSource(ClusterConfig dataSourceConfig) throws Exception {
		super(dataSourceConfig);
		for(DbDataSource ds : dbDataSources){
			if(ds.getConfig().isReadonly()){
				currentReadDataSource = ds;
				break;
			}
		}
	}
		
	protected DbDataSource getReadDataSource() throws SQLException{
		if (currentReadDataSource != null && currentReadDataSource.isAlive()) {
				return currentReadDataSource;			
		}
		
		for(int index = 0; index < dbDataSources.size(); index ++) {
			DbDataSource dataSource = dbDataSources.get(index);			
			if(dataSource == null) continue;			
			if (dataSource.isAlive() && (!dataSource.isFull())){
				return dataSource;
			}
		}
		
		throw new SQLException("SWAP no available datasource. " + this ,"08S01");
	}
}
