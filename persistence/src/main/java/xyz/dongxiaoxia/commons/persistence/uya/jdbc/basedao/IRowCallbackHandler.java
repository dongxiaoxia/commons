package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRowCallbackHandler {
	Object exec(ResultSet rs) throws SQLException;
}