package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class PreparedStatementWrapper extends StatementWrapper implements
PreparedStatement{
	
	/** Handle to the real prepared statement. */
	private PreparedStatement internalPreparedStatement;

	public PreparedStatementWrapper(PreparedStatement internalStatement,
			ConnectionWrapper connection) {
		super(internalStatement, connection);
		this.internalPreparedStatement = internalStatement;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		ResultSet result = null;
		try{
			result = this.internalPreparedStatement.executeQuery();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int executeUpdate() throws SQLException {
		int result = 0;
		try{
			result = this.internalPreparedStatement.executeUpdate();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		try{
			this.internalPreparedStatement.setNull(parameterIndex, sqlType);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		try{
			this.internalPreparedStatement.setBoolean(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		try{
			this.internalPreparedStatement.setByte(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		try{
			this.internalPreparedStatement.setShort(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		try{
			this.internalPreparedStatement.setInt(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		try{
			this.internalPreparedStatement.setLong(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		try{
			this.internalPreparedStatement.setFloat(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		try{
			this.internalPreparedStatement.setDouble(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		try{
			this.internalPreparedStatement.setBigDecimal(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		try{
			this.internalPreparedStatement.setString(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		try{
			this.internalPreparedStatement.setBytes(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		try{
			this.internalPreparedStatement.setDate(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		try{
			this.internalPreparedStatement.setTime(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		try{
			this.internalPreparedStatement.setTimestamp(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setAsciiStream(parameterIndex, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	@Deprecated
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setUnicodeStream(parameterIndex, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setBinaryStream(parameterIndex, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void clearParameters() throws SQLException {
		try{
			this.internalPreparedStatement.clearParameters();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		try{
			this.internalPreparedStatement.setObject(parameterIndex, x, targetSqlType);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		try{
			this.internalPreparedStatement.setObject(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public boolean execute() throws SQLException {
		boolean result = false;
		try{
			result = this.internalPreparedStatement.execute();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void addBatch() throws SQLException {
		try{
			this.internalPreparedStatement.addBatch();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setCharacterStream(parameterIndex, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		try{
			this.internalPreparedStatement.setRef(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		try{
			this.internalPreparedStatement.setBlob(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		try{
			this.internalPreparedStatement.setClob(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		try{
			this.internalPreparedStatement.setArray(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		ResultSetMetaData result = null;
		try{
			result = this.internalPreparedStatement.getMetaData();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		try{
			this.internalPreparedStatement.setDate(parameterIndex, x, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		try{
			this.internalPreparedStatement.setTime(parameterIndex, x, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		try{
			this.internalPreparedStatement.setTimestamp(parameterIndex, x, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		try{
			this.internalPreparedStatement.setNull(parameterIndex, sqlType, typeName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		try{
			this.internalPreparedStatement.setURL(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		ParameterMetaData result = null;
		try{
			result = this.internalPreparedStatement.getParameterMetaData();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		try{
			this.internalPreparedStatement.setRowId(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		try{
			this.internalPreparedStatement.setNString(parameterIndex, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		try{
			this.internalPreparedStatement.setNCharacterStream(parameterIndex, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		try{
			this.internalPreparedStatement.setNClob(parameterIndex, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setClob(parameterIndex, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setBlob(parameterIndex, inputStream, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setNClob(parameterIndex, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		try{
			this.internalPreparedStatement.setSQLXML(parameterIndex, xmlObject);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		try{
			this.internalPreparedStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setAsciiStream(parameterIndex, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		try{
			this.internalPreparedStatement.setBinaryStream(parameterIndex, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		try{
			this.internalPreparedStatement.setCharacterStream(parameterIndex, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		try{
			this.internalPreparedStatement.setAsciiStream(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		try{
			this.internalPreparedStatement.setBinaryStream(parameterIndex, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		try{
			this.internalPreparedStatement.setCharacterStream(parameterIndex, reader);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		try{
			this.internalPreparedStatement.setNCharacterStream(parameterIndex, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		try{
			this.internalPreparedStatement.setClob(parameterIndex, reader);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		try{
			this.internalPreparedStatement.setBlob(parameterIndex, inputStream);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		try{
			this.internalPreparedStatement.setNClob(parameterIndex, reader);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

}
