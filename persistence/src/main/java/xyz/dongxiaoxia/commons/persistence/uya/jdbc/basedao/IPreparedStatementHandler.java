package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IPreparedStatementHandler {
	public Object exec(PreparedStatement ps) throws SQLException;
}
