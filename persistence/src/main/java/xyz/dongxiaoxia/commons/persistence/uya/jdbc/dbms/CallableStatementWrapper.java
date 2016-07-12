package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * 对CallableStatement封装
 * @author renjun
 *
 */
public class CallableStatementWrapper extends PreparedStatementWrapper 
implements CallableStatement{
	
	/** Handle to the real callable statement. */
	private CallableStatement internalCallableStatement;

	public CallableStatementWrapper(CallableStatement internalStatement,
									ConnectionWrapper connection) {
		super(internalStatement, connection);
		this.internalCallableStatement = internalStatement;
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		try{
			this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		try{
			this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		
	}

	@Override
	public boolean wasNull() throws SQLException {				
		try{
			return this.internalCallableStatement.wasNull();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public String getString(int parameterIndex) throws SQLException {		
		try{
			return this.internalCallableStatement.getString(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public boolean getBoolean(int parameterIndex) throws SQLException {	
		try{
			return this.internalCallableStatement.getBoolean(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public byte getByte(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getByte(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public short getShort(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getShort(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public int getInt(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getInt(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public long getLong(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getLong(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public float getFloat(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getFloat(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public double getDouble(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getDouble(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		try{
			return this.internalCallableStatement.getBigDecimal(parameterIndex,scale);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public byte[] getBytes(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getBytes(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Date getDate(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getDate(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Time getTime(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getTime(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getTimestamp(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Object getObject(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getObject(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getBigDecimal(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Object getObject(int parameterIndex, Map<String, Class<?>> map)
			throws SQLException {
		try{
			return this.internalCallableStatement.getObject(parameterIndex, map);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Ref getRef(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getRef(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Blob getBlob(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getBlob(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Clob getClob(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getClob(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Array getArray(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getArray(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		try{
			return this.internalCallableStatement.getDate(parameterIndex, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		try{
			return this.internalCallableStatement.getTime(parameterIndex,cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {
		try{
			return this.internalCallableStatement.getTimestamp(parameterIndex, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType,
			String typeName) throws SQLException {
		try{
			this.internalCallableStatement.registerOutParameter(parameterIndex,sqlType,typeName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {
		try{
			this.internalCallableStatement.registerOutParameter(parameterName,sqlType);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		try{
			this.internalCallableStatement.registerOutParameter(parameterName,sqlType,scale);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		try{
			this.internalCallableStatement.registerOutParameter(parameterName,sqlType,typeName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public URL getURL(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getURL(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setURL(String parameterName, URL val) throws SQLException {
		try{
			this.internalCallableStatement.setURL(parameterName, val);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNull(String parameterName, int sqlType) throws SQLException {
		try{
			this.internalCallableStatement.setNull(parameterName, sqlType);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}		
	}

	@Override
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		try{
			this.internalCallableStatement.setBoolean(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public void setByte(String parameterName, byte x) throws SQLException {
		try{
			this.internalCallableStatement.setByte(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public void setShort(String parameterName, short x) throws SQLException {
		try{
			this.internalCallableStatement.setShort(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public void setInt(String parameterName, int x) throws SQLException {
		try{
			this.internalCallableStatement.setInt(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setLong(String parameterName, long x) throws SQLException {
		try{
			this.internalCallableStatement.setLong(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setFloat(String parameterName, float x) throws SQLException {
		try{
			this.internalCallableStatement.setFloat(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setDouble(String parameterName, double x) throws SQLException {
		try{
			this.internalCallableStatement.setDouble(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {
		try{
			this.internalCallableStatement.setBigDecimal(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setString(String parameterName, String x) throws SQLException {
		try{
			this.internalCallableStatement.setString(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		try{
			this.internalCallableStatement.setBytes(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setDate(String parameterName, Date x) throws SQLException {
		try{
			this.internalCallableStatement.setDate(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTime(String parameterName, Time x) throws SQLException {
		try{
			this.internalCallableStatement.setTime(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {
		try{
			this.internalCallableStatement.setTimestamp(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {
		try{
			this.internalCallableStatement.setAsciiStream(parameterName, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {
		try{
			this.internalCallableStatement.setBinaryStream(parameterName, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {
		try{
			this.internalCallableStatement.setObject(parameterName, x, targetSqlType, scale);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {
		try{
			this.internalCallableStatement.setObject(parameterName, x, targetSqlType);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}		
	}

	@Override
	public void setObject(String parameterName, Object x) throws SQLException {
		try{
			this.internalCallableStatement.setObject(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		try{
			this.internalCallableStatement.setCharacterStream(parameterName, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {
		try{
			this.internalCallableStatement.setDate(parameterName, x, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {
		try{
			this.internalCallableStatement.setTime(parameterName, x, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {
		try{
			this.internalCallableStatement.setTimestamp(parameterName, x, cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		try{
			this.internalCallableStatement.setNull(parameterName, sqlType, typeName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public String getString(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getString(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public boolean getBoolean(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getBoolean(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public byte getByte(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getByte(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public short getShort(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getShort(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public int getInt(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getInt(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public long getLong(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getLong(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public float getFloat(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getFloat(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public double getDouble(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getDouble(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public byte[] getBytes(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getBytes(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Date getDate(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getDate(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Time getTime(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getTime(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Timestamp getTimestamp(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getTimestamp(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Object getObject(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getObject(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getBigDecimal(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException {
		try{
			return this.internalCallableStatement.getObject(parameterName, map);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Ref getRef(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getRef(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Blob getBlob(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getBlob(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Clob getClob(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getClob(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Array getArray(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getArray(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		try{
			return this.internalCallableStatement.getDate(parameterName,cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		try{
			return this.internalCallableStatement.getTime(parameterName,cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException {
		try{
			return this.internalCallableStatement.getTimestamp(parameterName,cal);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public URL getURL(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getURL(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public RowId getRowId(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getRowId(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public RowId getRowId(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getRowId(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException {
		try{
			this.internalCallableStatement.setRowId(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNString(String parameterName, String value)
			throws SQLException {
		try{
			this.internalCallableStatement.setNString(parameterName, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		try{
			this.internalCallableStatement.setNCharacterStream(parameterName, value,length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException {
		try{
			this.internalCallableStatement.setNClob(parameterName, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		try{
			this.internalCallableStatement.setClob(parameterName, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		try{
			this.internalCallableStatement.setBlob(parameterName, inputStream, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		try{
			this.internalCallableStatement.setNClob(parameterName, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public NClob getNClob(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getNClob(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public NClob getNClob(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getNClob(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		try{
			this.internalCallableStatement.setSQLXML(parameterName, xmlObject);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getSQLXML(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getSQLXML(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public String getNString(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getNString(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public String getNString(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getNString(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getNCharacterStream(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getNCharacterStream(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		try{
			return this.internalCallableStatement.getCharacterStream(parameterIndex);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException {
		try{
			return this.internalCallableStatement.getCharacterStream(parameterName);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException {
		try{
			this.internalCallableStatement.setBlob(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setClob(String parameterName, Clob x) throws SQLException {
		try{
			this.internalCallableStatement.setClob(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		try{
			this.internalCallableStatement.setAsciiStream(parameterName, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		try{
			this.internalCallableStatement.setBinaryStream(parameterName, x, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		try{
			this.internalCallableStatement.setCharacterStream(parameterName, reader, length);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		try{
			this.internalCallableStatement.setAsciiStream(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		try{
			this.internalCallableStatement.setBinaryStream(parameterName, x);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		try{
			this.internalCallableStatement.setCharacterStream(parameterName, reader);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		try{
			this.internalCallableStatement.setNCharacterStream(parameterName, value);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		try{
			this.internalCallableStatement.setClob(parameterName, reader);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		try{
			this.internalCallableStatement.setBlob(parameterName, inputStream);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		try{
			this.internalCallableStatement.setNClob(parameterName, reader);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        try{
            return this.internalCallableStatement.getObject(parameterIndex, type);
        } catch (Throwable t) {
            throw this.connection.markPossiblyBroken(t);
        }
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        try{
            return this.internalCallableStatement.getObject(parameterName, type);
        } catch (Throwable t) {
            throw this.connection.markPossiblyBroken(t);
        }
    }


}
