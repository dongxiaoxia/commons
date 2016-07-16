package xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.OutSQL;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlServerPSCreater extends PSCreaterBase {

	@Override
	public <I> PreparedStatement createDelete(Class<?> clazz, 
			Connection conn,
			I id,
			OutSQL sql) throws Exception {
		
		String idColumnName = "";
		List<Field> fieldList = Common.getIdFields(clazz);
		if (fieldList.size() != 1) {
			throw new Exception("无法根据主键删除：主键不存在 或 有两个以上的主键");
		} else {
			idColumnName = Common.getDBCloumnName(clazz, fieldList.get(0));
		}

		StringBuffer sbSql = new StringBuffer("DELETE FROM ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WHERE ");
		sbSql.append("[");
		sbSql.append(idColumnName);
		sbSql.append("]");
		sbSql.append("=?");
		
		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		
		Common.setPara(ps, id, 1);
		
		return ps;
	}

	@Override
	public PreparedStatement createDeleteByCustom(Class<?> clazz,
			Connection conn, 
			String condition,
			OutSQL sql) throws Exception {
		
		StringBuffer sbSql = new StringBuffer("DELETE FROM ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WHERE ");
		if (condition == null || condition == "") {
			condition = "1=2";
		}
		sbSql.append(condition);
		
		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		return ps;
	}
 

	@Override
	public PreparedStatement createUpdateByCustom(Class<?> clazz,
			Connection conn, 
			String updateStatement, 
			String condition,
			OutSQL sql)
			throws Exception {

		StringBuffer sbSql = new StringBuffer("UPDATE ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WITH(ROWLOCK) SET ");
		sbSql.append(updateStatement);
		sbSql.append(" WHERE ");
		if (condition == null || condition.trim().equals("")) {
			condition = "1=2";
		}
		sbSql.append(condition);

		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		return ps;
	}

	@Override
	public PreparedStatement createUpdateEntity(Object bean, Connection conn, OutSQL sql)
			throws Exception {
		
		Class<?> clazz = bean.getClass();
		List<Field> idFields = Common.getIdFields(clazz);
		if (idFields.size() == 0) {
			throw new Exception("无法根据实体更新：主键不存在 ");
		}

		List<Field> listField = Common.getUpdatableFields(clazz);
		if (listField.size() > 0) {
			StringBuffer sbSql = new StringBuffer("UPDATE ");
			sbSql.append(Common.getTableName(clazz));
			sbSql.append(" WITH(ROWLOCK) ");
			boolean isFirst = true;
			
			for (int i = 0; i < listField.size(); i++) {
				if (isFirst) {
					sbSql.append(" SET ");
				} else {
					sbSql.append(", ");
				}
				sbSql.append("[");
				sbSql.append(Common.getDBCloumnName(clazz, listField.get(i)));
				sbSql.append("]");
				sbSql.append("=?");
				isFirst = false;
			}

			sbSql.append(" WHERE ");
			isFirst = true;
			for (int i = 0; i < idFields.size(); i++) {
				if (!isFirst) {
					sbSql.append(" AND ");
				}
				sbSql.append("[");
				sbSql.append(Common.getDBCloumnName(clazz, idFields.get(i)));
				sbSql.append("]");
				sbSql.append("=?");
				isFirst = false;
			}
			
			sql.setSql(sbSql.toString());
			PreparedStatement ps = conn.prepareStatement(sql.getSql());
			
			int index = 1;
			for (int i = 0; i < listField.size(); i++) {
				Method m = Common.getGetterMethod(clazz, listField.get(i));
				Object value = m.invoke(bean, new Object[] {});
				Common.setPara(ps, value, index);
				index++;
			}
			
			for (int i = 0; i < idFields.size(); i++) {
				Method m = Common.getGetterMethod(clazz, idFields.get(i));
				Object value = m.invoke(bean, new Object[] {});
				Common.setPara(ps, value, index);
				index++;
			}
			
			return ps;
			
		} else {
			throw new Exception("表实体没有字段");
		}
	}

	@Override
	public PreparedStatement createInsert(Object bean, Connection conn, OutSQL sql)
			throws Exception {

		Class<?> clazz = bean.getClass();
		StringBuffer sbSql = new StringBuffer("INSERT INTO ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append("(");
		List<Field> listField = Common.getInsertableFields(clazz);
		
		StringBuilder sbColumn = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();
		boolean isFirst = true;
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < listField.size(); i++) {
			Field field = listField.get(i);
			Method m = Common.getGetterMethod(clazz, field);
			Object value = m.invoke(bean, new Object[] {});
			if (value == null)
				continue;
			
			values.add(value);
			
			if (!isFirst) {
				sbColumn.append(", ");
				sbValue.append(", ");
			}
			sbColumn.append("[");
			sbColumn.append(Common.getDBCloumnName(clazz, field));
			sbColumn.append("]");

			
			sbValue.append("?");
			isFirst = false;
		}
		
		sbSql.append(sbColumn);
		sbSql.append(") VALUES (");
		sbSql.append(sbValue);
		sbSql.append(")");
		
		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql(), Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 0; i < values.size(); i++) {
			Common.setPara(ps, values.get(i), i+1);
		}
		
		return ps;
	}

	
	@Override
	public PreparedStatement createGetByCustom(Class<?> clazz, 
			Connection conn,
			String columns, 
			String condition, 
			String orderBy,
			OutSQL sql) throws Exception {
		
		StringBuffer sbSql = new StringBuffer("SELECT ");
		if (columns == null || columns.trim().equals("")) {
			sbSql.append("*");
		} else {
			sbSql.append(columns);
		}
		sbSql.append(" FROM ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WITH(NOLOCK) ");
		if (condition != null && !condition.trim().equals("")) {
			sbSql.append(" WHERE ");
			sbSql.append(condition);
		}
		
		if(orderBy != null && !orderBy.trim().equals("")) {
			sbSql.append(" ORDER BY ");
			sbSql.append(orderBy);
		}
		
		sql.setSql(sbSql.toString());

		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		return ps;
	}

	@Override
	public <I> PreparedStatement createGetEntity(Class<?> clazz,
			Connection conn, 
			I id,
			OutSQL sql) throws Exception {
		String idColumnName = "";
		List<Field> fieldList = Common.getIdFields(clazz);
		if (fieldList.size() != 1) {
			throw new Exception("无法根据主键ID获取数据：主键不存在 或 有两个以上的主键");
		} else {
			idColumnName = Common.getDBCloumnName(clazz, fieldList.get(0));
		}
		StringBuffer sbSql = new StringBuffer("SELECT * ");

		sbSql.append(" FROM ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WITH(NOLOCK) ");
		sbSql.append(" WHERE [");
		sbSql.append(idColumnName);
		sbSql.append("]");
		
		sbSql.append("=?");

		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		Common.setPara(ps, id, 1);
		return ps;
	}
	
	@Override
	public PreparedStatement createGetByPage(Class<?> clazz, 
			Connection conn,
			String condition, 
			String columns, 
			int start, 
			int pageSize,
			String orderBy,
			OutSQL sql) throws Exception {
		
		if(orderBy == null || orderBy.trim().equalsIgnoreCase("")){
			List<Field> fieldList = Common.getIdFields(clazz);
			for(int i=0; i<fieldList.size(); i++) {
				if(i != 0) {
					orderBy += ",";
				}
				orderBy += Common.getDBCloumnName(clazz, fieldList.get(i)) + " DESC ";
			}
		}
		
//		int preExclude = pageSize * (page - 1);
//		int preTotal = pageSize * page;

		String tableName = Common.getTableName(clazz);

		StringBuffer sbSql = new StringBuffer("SELECT ");
		if (columns == null || columns.trim().equalsIgnoreCase("")) {
			sbSql.append("*");
		} else {
			sbSql.append(columns);
		}
		sbSql.append(" FROM ");
		sbSql.append(" (SELECT ROW_NUMBER() OVER(");
		
		sbSql.append(" ORDER BY ");
		sbSql.append(orderBy);
		sbSql.append(") AS rownum, * FROM ");
		sbSql.append(tableName);
		sbSql.append(" WITH (NOLOCK) WHERE ");
		if (condition == null || condition.trim().equals("")) {
			condition = "1=1";
		}
		sbSql.append(condition);
		sbSql.append(") ");
		sbSql.append("AS D WHERE rownum >?");
		sbSql.append(" AND rownum<=?");
		sbSql.append(" ORDER BY ");
		sbSql.append(orderBy);

		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
//		ps.setInt(1, preExclude);
//		ps.setInt(2, preTotal);
		ps.setInt(1, start);
		ps.setInt(2, start + pageSize);
		return ps;
	}
	
	@Override
	public PreparedStatement createGetCount(Class<?> clazz, 
			Connection conn,
			String condition,
			OutSQL sql) throws Exception {
		StringBuffer sbSql = new StringBuffer("SELECT COUNT(0) FROM ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WITH(NOLOCK) ");
		if (condition != null && !condition.trim().equals("")) {
			sbSql.append(" WHERE ");
			sbSql.append(condition);
		}

		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		return ps;
	}
	
	
	
	
	@Override
	public <I> PreparedStatement createDeleteByIDS(Class<?> clazz,
			Connection conn, I[] ids, OutSQL sql) throws Exception {
		StringBuffer sbSql = new StringBuffer("DELETE FROM ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WHERE ");
		
		List<Field> fieldList = Common.getIdFields(clazz);
		if (fieldList.size() != 1) {
			throw new Exception("无法根据主键ID删除数据：主键不存在 或 有两个以上的主键");
		} else {
			sbSql.append(Common.getDBCloumnName(clazz, fieldList.get(0)));
		}
		sbSql.append(" IN (");
		for(int i=0; i<ids.length; i++) {
			if(i > 0) {
				sbSql.append(",");
			}
			sbSql.append("?");
		}
		sbSql.append(")");
		
		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		int index = 1;
		for(int i=0; i<ids.length; i++, index++) {
			Common.setPara(ps, ids[i], index);
		}
		
		return ps;
	}

	@Override
	public <I> PreparedStatement createUpdateByID(Class<?> clazz,
			Connection conn, 
			String updateStatement, 
			I id, 
			OutSQL sql)
			throws Exception {

		String idName = null;
		List<Field> fieldList = Common.getIdFields(clazz);
		if (fieldList.size() != 1) {
			throw new Exception("无法根据主键ID删除数据：主键不存在 或 有两个以上的主键");
		} else {
			idName = Common.getDBCloumnName(clazz, fieldList.get(0));
		}
		
		StringBuffer sbSql = new StringBuffer("UPDATE ");
		sbSql.append(Common.getTableName(clazz));
		sbSql.append(" WITH(ROWLOCK) SET ");
		sbSql.append(updateStatement);
		sbSql.append(" WHERE ");
		sbSql.append(idName);
		sbSql.append("=?");
		
		sql.setSql(sbSql.toString());
		PreparedStatement ps = conn.prepareStatement(sql.getSql());
		Common.setPara(ps, id, 1);
		return ps;
	}

}