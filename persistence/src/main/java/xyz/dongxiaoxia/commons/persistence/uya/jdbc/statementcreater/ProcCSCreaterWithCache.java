package xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater;


import xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation.ProcedureName;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.OutSQL;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcCSCreaterWithCache extends PSCreaterBase {
	
	private static Map<String,CallableStatement> mapStatement = null;
	
	static {
		mapStatement = new HashMap<String,CallableStatement>();
	}
	
	public synchronized void addStatement(String key, CallableStatement cs) {
		if(!mapStatement.containsKey(key)) {
			mapStatement.put(key, cs);
		}
	}

	@Override
	public <I> CallableStatement createDelete(Class<?> clazz, 
			Connection connection, 
			I id,
			OutSQL sql) throws Exception {
		List<Field> fieldList = Common.getIdFields(clazz);
		if (fieldList.size() != 1) {
			throw new Exception("无法根据主键删除：主键不存在 或 有两个以上的主键");
		} else {
			ProcedureName des = Common.getProc(clazz);
			if(des != null){
				CallableStatement cstmt = mapStatement.get(des.delete());
				if(cstmt == null) {
					StringBuffer sbSql = new StringBuffer("{call ");
					sbSql.append(des.delete());
					sbSql.append("(?,?,?)");
					sbSql.append("}");
					
					sql.setSql(sbSql.toString());
					cstmt = connection.prepareCall(sql.getSql());
				}
				String columnName = Common.getDBCloumnName(clazz, fieldList.get(0));
				cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
				cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
				cstmt.setString("Where", "["+columnName + "]=" + Common.getValue(id));
				
				return cstmt;
			}else{
				throw new Exception("实体没有定义:@ProcedureName");
			}
		}
	}

	@Override
	public CallableStatement createDeleteByCustom(Class<?> clazz, 
			Connection connection, 
			String condition,
			OutSQL sql)
			throws Exception {
		ProcedureName des = Common.getProc(clazz);
		if(des != null){
			CallableStatement cstmt = mapStatement.get(des.delete());
			if(cstmt == null) {
				StringBuffer sbSql = new StringBuffer("{call ");
				sbSql.append(des.delete());
				sbSql.append("(?,?,?)");
				sbSql.append("}");
				
				sql.setSql(sbSql.toString());
				cstmt = connection.prepareCall(sql.getSql());
			}
			
			cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
			cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
			cstmt.setString("Where", condition);
			
			return cstmt;
		}else{
			throw new Exception("实体没有定义:@ProcedureName");
		}
	}

	@Override
	public CallableStatement createGetByCustom(Class<?> clazz, 
			Connection connection, 
			String columns,
			String condition, 
			String orderBy,
			OutSQL sql) throws Exception {
		
		ProcedureName des = Common.getProc(clazz);
		if(des != null){			
			CallableStatement cstmt = mapStatement.get(des.load());
			if(cstmt == null) {
				StringBuffer sbSql = new StringBuffer("{call ");
				sbSql.append(des.load());
				sbSql.append("(?,?,?,?,?)");
				sbSql.append("}");
				
				sql.setSql(sbSql.toString());
				cstmt = connection.prepareCall(sql.getSql());
			}
			
			cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
			cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
			cstmt.setString("OrderByFields", orderBy);
			cstmt.setString("SelectColumns", columns);
			cstmt.setString("Where", condition);
			
			return cstmt;
		}else{
			throw new Exception("实体没有定义:@ProcedureName");
		}
	}

	@Override
	public CallableStatement createGetByPage(Class<?> clazz, 
			Connection connection, 
			String condition,
			String columns, 
			int start, 
			int pageSize, 
			String orderBy,
			OutSQL sql) throws Exception {

		ProcedureName des = Common.getProc(clazz);
		if(des != null){
			CallableStatement cstmt = mapStatement.get(des.locaByPage());
			if(cstmt == null) {
				StringBuffer sbSql = new StringBuffer("{call ");
				sbSql.append(des.locaByPage());
				sbSql.append("(?,?,?,?,?,?,?)");
				sbSql.append("}");
				
				sql.setSql(sbSql.toString());
				cstmt = connection.prepareCall(sql.getSql());
			}
			
			cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
			cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
			
			cstmt.setInt("PageSize", pageSize);
			
//			cstmt.setInt("CurrentPage", page);
			int currentPage = start%pageSize>0?((int)(start/pageSize))+1:start/pageSize;
			cstmt.setInt("CurrentPage", currentPage);
			cstmt.setString("SelectColumns", columns);
			cstmt.setString("OrderByFields", orderBy);
			cstmt.setString("Where", condition);
			
			return cstmt;
		}else{
			throw new Exception("实体没有定义:@ProcedureName");
		}
	}

	@Override
	public <I> CallableStatement createGetEntity(Class<?> clazz, 
			Connection connection, 
			I id,
			OutSQL sql) throws Exception {
		List<Field> fieldList = Common.getIdFields(clazz);
		if (fieldList.size() != 1) {
			throw new Exception("无法根据主键ID获取数据：主键不存在 或 有两个以上的主键");
		} else {
			ProcedureName des = Common.getProc(clazz);
			if(des != null){

				CallableStatement cstmt = mapStatement.get(des.load());
				if(cstmt == null) {
					StringBuffer sbSql = new StringBuffer("{call ");
					sbSql.append(des.load());
					sbSql.append("(?,?,?,?,?)");
					sbSql.append("}");
					
					sql.setSql(sbSql.toString());
					cstmt = connection.prepareCall(sql.getSql());
				}
				
				String columnName = Common.getDBCloumnName(clazz, fieldList.get(0));
				cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
				cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
				cstmt.setString("OrderByFields", "");
				cstmt.setString("SelectColumns", "*");
				cstmt.setString("Where", "["+columnName + "]=" + Common.getValue(id));
				
				return cstmt;
			}else{
				throw new Exception("实体没有定义:@ProcedureName");
			}
		}
	}
	
	@Override
	public CallableStatement createUpdateByCustom(Class<?> clazz, 
			Connection connection, 
			String updateStatement,
			String condition,
			OutSQL sql) throws Exception {

		ProcedureName des = Common.getProc(clazz);
		if(des != null){
			CallableStatement cstmt = mapStatement.get(des.update());
			if(cstmt == null) {
				StringBuffer sbSql = new StringBuffer("{call ");
				sbSql.append(des.update());
				sbSql.append("(?,?,?,?)");
				sbSql.append("}");
				sql.setSql(sbSql.toString());
				
				cstmt = connection.prepareCall(sql.getSql());
			}
			
			cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
			cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
			cstmt.setString("UpdateStatement", updateStatement);
			cstmt.setString("Where", condition);
			
			return cstmt;
		}else{
			throw new Exception("实体没有定义:@ProcedureName");
		}
	}

	@Override
	public CallableStatement createUpdateEntity(Object bean, 
			Connection connection,
			OutSQL sql) throws Exception {
		List<Field> fieldList = Common.getAllFields(bean.getClass());
		if(fieldList.size()>0){
			ProcedureName des = Common.getProc(bean.getClass());
			if(des != null){
				CallableStatement cstmt = mapStatement.get(des.updateByID());
				if(cstmt == null) {
					StringBuffer sbSql = new StringBuffer("{call ");
					sbSql.append(des.updateByID());
					sbSql.append("(?,?");
					for(int i=0;i< fieldList.size();i++){
						sbSql.append(",?");
					}
					sbSql.append(")");
					sbSql.append("}");
					
					sql.setSql(sbSql.toString());
					cstmt = connection.prepareCall(sql.getSql());
				}
				
				cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
				cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
				
				for(Field f :fieldList ){
					Common.setPara(cstmt, bean, f);
				}
				return cstmt;
			}else{
				throw new Exception("实体没有定义:@ProcedureName");
			}
		}
		else{
			throw new Exception("表实体没有字段");
		}
	}

	@Override
	public CallableStatement createInsert(Object bean, 
			Connection connection,
			OutSQL sql) throws Exception {
		Class<?> clazz = bean.getClass();
		List<Field> fieldList = Common.getInsertableFields(clazz);
		List<Field> idFields = Common.getIdentityFields(clazz);
		if(fieldList.size()>0 && (idFields == null || idFields.size()<=1)){
			
			ProcedureName des = Common.getProc(clazz);
			if(des != null){
				
				CallableStatement cstmt = mapStatement.get(des.insert());
				if(cstmt == null) {
					StringBuffer sbSql = new StringBuffer("{call ");
					sbSql.append(des.insert());
					sbSql.append("(?,?");
					if(idFields != null && idFields.size()==1){
						sbSql.append(",?");
					}
					for(int i=0; i< fieldList.size(); i++){
						sbSql.append(",?");
					}
					sbSql.append(")");
					sbSql.append("}");
					
					sql.setSql(sbSql.toString());
					cstmt = connection.prepareCall(sql.getSql());
				}

				cstmt.registerOutParameter("ReturnValue", java.sql.Types.INTEGER);
				cstmt.registerOutParameter("RowCount", java.sql.Types.INTEGER);
				if(idFields != null && idFields.size()==1){
					String idColumn = Common.getDBCloumnName(bean.getClass(), idFields.get(0));
					Class<?> idColumnType = idFields.get(0).getType();
					if(idColumnType.equals(long.class) || idColumnType.equals(Long.class)){
						cstmt.registerOutParameter(idColumn, java.sql.Types.BIGINT);
					}
					else if(idColumnType.equals(int.class) || idColumnType.equals(Integer.class)){
						cstmt.registerOutParameter(idColumn, java.sql.Types.INTEGER);
					}
					else if(idColumnType.equals(String.class)){
						cstmt.registerOutParameter(idColumn, java.sql.Types.VARCHAR);
					}
					else if(idColumnType.equals(Short.class) || idColumnType.equals(short.class)){
						cstmt.registerOutParameter(idColumn, java.sql.Types.SMALLINT);
					}
					else if(idColumnType.equals(Byte.class) || idColumnType.equals(byte.class)){
						cstmt.registerOutParameter(idColumn, java.sql.Types.BIT);
					}
					else {
						throw new Exception("未知的主键类型： com.baiyz.lib.dao.SqlCreate.ProcParaCreate");
					}
				}
				
				for(Field f : fieldList){
					Common.setPara(cstmt, bean, f);
				}
				return cstmt;
			} else{
				throw new Exception("实体没有定义:@ProcedureName");
			}
		}
		else{
			throw new Exception("表实体没有字段，或有两个以上的ID字段");
		}
	}
	
	@Override
	public PreparedStatement createGetCount(Class<?> clazz, 
			Connection conn,
			String condition,
			OutSQL sql) throws Exception {
		throw new Exception("not implement");
	}
	
	


	
	@Override
	public <I> PreparedStatement createDeleteByIDS(Class<?> clazz,
			Connection conn, I[] ids, OutSQL sql) throws Exception {
		throw new UnsupportedOperationException("Not supported for proc");
	}

	@Override
	public <I> PreparedStatement createUpdateByID(Class<?> clazz,
			Connection conn, String updateStatement, I id, OutSQL sql)
			throws Exception {
		throw new UnsupportedOperationException("Not supported for proc");
	}
}