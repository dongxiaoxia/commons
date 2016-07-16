package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.sql.Connection;

import com.baiyz.www.lib.dao.statementcreater.IStatementCreater;

public interface ITransactionHandler {
	public Object exec(Connection conn, IStatementCreater sqlServerCreater, IStatementCreater mysqlCreater) throws Exception;
}
