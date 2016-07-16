package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface ICallableStatementHandler {
	public Object exec(CallableStatement cs) throws SQLException;
}