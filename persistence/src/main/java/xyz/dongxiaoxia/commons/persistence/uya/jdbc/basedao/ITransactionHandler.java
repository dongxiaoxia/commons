package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;


import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;

import java.sql.Connection;

public interface ITransactionHandler {
    Object exec(Connection conn, IStatementCreater sqlServerCreater, IStatementCreater mysqlCreater) throws Exception;
}
