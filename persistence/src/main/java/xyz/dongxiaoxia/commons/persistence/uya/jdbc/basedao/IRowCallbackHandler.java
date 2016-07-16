package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRowCallbackHandler {
	public Object exec(ResultSet rs) throws SQLException;
}