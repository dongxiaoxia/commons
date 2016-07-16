package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import java.sql.*;
import java.util.List;


public class StatementWrapper implements Statement {
	
//	protected boolean logicalClosed = false;
	
	/** A handle to the actual statement. */
	protected Statement internalStatement;
	
	/** SQL Statement used for this statement. */
//	protected String sql;
	
	protected ConnectionWrapper connection;
	
	public StatementWrapper(Statement internalStatement, String sql,
			ConnectionWrapper connection){
//		this.sql = sql;
		this.internalStatement = internalStatement;
		this.connection = connection;
	}
	
	public StatementWrapper(Statement internalStatement,
			ConnectionWrapper connection){
		this(internalStatement, null, connection);
	}
	
	protected List<DbMonitor> getMonitors(){
		return this.connection.getMonitors();
	}

    /**
	 * 以下是Statement
	 */
	

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		T result = null;
		try{
			result = this.internalStatement.unwrap(iface);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.isWrapperFor(iface);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		ResultSet result = null;
		try{
			result = this.internalStatement.executeQuery(sql);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.executeUpdate(sql);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void close() throws SQLException {
		try{
			this.internalStatement.close();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getMaxFieldSize();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		try{
			this.internalStatement.setMaxFieldSize(max);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
		
	}

	@Override
	public int getMaxRows() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getMaxRows();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		try{
			this.internalStatement.setMaxRows(max);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		try{
			this.internalStatement.setEscapeProcessing(enable);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getQueryTimeout();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		try{
			this.internalStatement.setQueryTimeout(seconds);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}	
	}

	@Override
	public void cancel() throws SQLException {
		try{
			this.internalStatement.cancel();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}		
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		SQLWarning result = null;
		try{
			result = this.internalStatement.getWarnings();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void clearWarnings() throws SQLException {
		try{
			this.internalStatement.clearWarnings();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}		
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		try{
			this.internalStatement.setCursorName(name);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}		
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.execute(sql);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		ResultSet result = null;
		try{
			result = this.internalStatement.getResultSet();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getUpdateCount();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.getMoreResults();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		try{
			this.internalStatement.setFetchDirection(direction);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}		
	}

	@Override
	public int getFetchDirection() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getFetchDirection();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		try{
			this.internalStatement.setFetchSize(rows);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public int getFetchSize() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getFetchSize();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getResultSetConcurrency();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int getResultSetType() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getResultSetType();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		try{
			this.internalStatement.addBatch(sql);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public void clearBatch() throws SQLException {
		try{
			this.internalStatement.clearBatch();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public int[] executeBatch() throws SQLException {
		int[] result = null;
		try{
			result = this.internalStatement.executeBatch();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.connection;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.getMoreResults(current);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		ResultSet result = null;
		try{
			result = this.internalStatement.getGeneratedKeys();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.executeUpdate(sql, autoGeneratedKeys);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.executeUpdate(sql, columnIndexes);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.executeUpdate(sql, columnNames);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.execute(sql, autoGeneratedKeys);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.execute(sql, columnIndexes);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.execute(sql, columnNames);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		int result = 0;
		try{
			result = this.internalStatement.getResultSetHoldability();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean isClosed() throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.isClosed();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		try{
			this.internalStatement.setPoolable(poolable);
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
	}

	@Override
	public boolean isPoolable() throws SQLException {
		boolean result = false;
		try{
			result = this.internalStatement.isPoolable();
		} catch (Throwable t) {
			throw this.connection.markPossiblyBroken(t);
		}
		return result;
	}

    @Override
    public void closeOnCompletion() throws SQLException {
        try{
            this.internalStatement.closeOnCompletion();
        } catch (Throwable t) {
            throw this.connection.markPossiblyBroken(t);
        }
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        boolean result = false;
        try{
            result = this.internalStatement.isCloseOnCompletion();
        } catch (Throwable t) {
            throw this.connection.markPossiblyBroken(t);
        }
        return result;
    }

}
