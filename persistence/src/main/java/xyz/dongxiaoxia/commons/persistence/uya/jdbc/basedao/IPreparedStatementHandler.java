package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IPreparedStatementHandler {
	Object exec(PreparedStatement ps) throws SQLException;
}
