package xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.OutSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public interface IStatementCreater {

    <I> PreparedStatement createDelete(Class<?> clazz, Connection conn, I id, OutSQL sql) throws Exception;

    <I> PreparedStatement createDeleteByIDS(Class<?> clazz, Connection conn, I[] ids, OutSQL sql) throws Exception;

    PreparedStatement createDeleteByCustom(Class<?> clazz, Connection conn, String condition, OutSQL sql) throws Exception;

    PreparedStatement createDeleteByCustom(Class<?> clazz, Connection conn, Map<String, Object> condition, OutSQL sql) throws Exception;

    PreparedStatement createGetByCustom(Class<?> clazz, Connection conn, String columns, String condition, String orderBy, OutSQL sql) throws Exception;

    PreparedStatement createGetByCustom(Class<?> clazz, Connection conn, String columns, Map<String, Object> condition, String orderBy, OutSQL sql) throws Exception;


    <I> PreparedStatement createGetByIDS(Class<?> clazz, Connection conn, I[] ids, OutSQL sql) throws Exception;

    PreparedStatement createGetByPage(Class<?> clazz, Connection conn, String condition, String columns, int start, int pageSize, String orderBy, OutSQL sql) throws Exception;

    PreparedStatement createGetByPage(Class<?> clazz, Connection conn, Map<String, Object> condition, String columns, int start, int pageSize, String orderBy, OutSQL sql) throws Exception;


    <I> PreparedStatement createGetEntity(Class<?> clazz, Connection conn, I id, OutSQL sql) throws Exception;

    PreparedStatement createUpdateByCustom(Class<?> clazz, Connection conn, String updateStatement, String condition, OutSQL sql) throws Exception;

    PreparedStatement createUpdateByCustom(Class<?> clazz, Connection conn, Map<String, Object> updateStatement, Map<String, Object> condition, OutSQL sql) throws Exception;


    PreparedStatement createUpdateEntity(Object bean, Connection conn, OutSQL sql) throws Exception;

    <I> PreparedStatement createUpdateByID(Class<?> clazz, Connection conn, String updateStatement, I id, OutSQL sql) throws Exception;

    PreparedStatement createInsert(Object bean, Connection conn, OutSQL sql) throws Exception;

    PreparedStatement createGetCount(Class<?> clazz, Connection conn, String condition, OutSQL sql) throws Exception;

    PreparedStatement createGetCount(Class<?> clazz, Connection conn, Map<String, Object> condition, OutSQL sql) throws Exception;
}