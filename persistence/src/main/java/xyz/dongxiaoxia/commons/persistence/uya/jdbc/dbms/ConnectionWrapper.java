package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;


public class ConnectionWrapper implements Connection  {

	/** Connection handle. */
	public Connection connection = null;
	
	/** Last time this connection was used by an application. */
	private long connectionLastUsed = System.currentTimeMillis();
	/** Last time we sent a reset to this connection. */
	private long connectionLastReset = System.currentTimeMillis();
	/** Time when this connection was created. */
	private long connectionCreationTime = System.currentTimeMillis();
	
	private DbDataSource dataSource;
	
	protected boolean isBroken = false;
	
	protected boolean logicalClosed = false;
	
	
	public ConnectionWrapper(DbDataSource dataSource,
							 Connection connection)
	throws SQLException {
		this.dataSource = dataSource;
		this.connection = connection;
	}
	
	protected void renew(){
		this.logicalClosed = false;
	}
	
	/**
	 * Close off the connection.
	 * 
	 * @throws SQLException
	 */
	protected void internalClose() throws SQLException {
		try {
			this.logicalClosed = true;
			
			if (this.connection != null){ // safety!
				
//				// 2011-05-30
//				if (this.isBroken) {
//					this.logger.error("SWAP Closed connection is broken.");
//				} else
//					this.logger.error("SWAP closed connection is normal.");
				
				
				
				this.connection.close();
			}

		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}
			
	
	public void setDataSource(DbDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DbDataSource getDataSource() {
		return dataSource;
	}
	
	protected SQLException  markPossiblyBroken(Throwable t) {
		SQLException e;
		if (t instanceof SQLException){
			e=(SQLException) t;
		} else {
			e = new SQLException(t == null ? "Unknown error" : t.getMessage(), "08999");
			e.initCause( t );
		}

		// 测试数据库连接是否已经关闭
		try{
			isBroken = this.connection.isClosed();
		} catch (SQLException closeException) {
			isBroken = true;
		}
		
//		if (isBroken){
//			if (e != null){
//				logger.error("SWAP this connection is broken.");
//				logger.error(e.getMessage());
//				e.printStackTrace();
//			}
//			else
//				this.logger.error("SWAP connection is broken, caused by null");
//		}
		
		for(DbMonitor monitor : this.getMonitors())
			monitor.onException(dataSource, this, t);
		
		return e;

//		String state = e.getSQLState();
//		if (state == null){ // safety;
//			state = "Z";
//		}

		// SQL-92 says:
		//		 Class values that begin with one of the <digit>s '5', '6', '7',
		//         '8', or '9' or one of the <simple Latin upper case letter>s 'I',
		//         'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
		//         'W', 'X', 'Y', or 'Z' are reserved for implementation-specified
		//         conditions.


		//		char firstChar = state.charAt(0);
		// if it's a communication exception, a mysql deadlock or an implementation-specific error code, flag this connection as being potentially broken.
		// state == 40001 is mysql specific triggered when a deadlock is detected
		// state == HY000 is firebird specific triggered when a connection is broken
//		char firstChar = state.charAt(0);
//		if (state.equals("40001") || 
//				state.equals("HY000") ||
//				state.startsWith("08") ||  (firstChar >= '5' && firstChar <='9') || (firstChar >='I' && firstChar <= 'Z')){
//			this.possiblyBroken = true;
//		}

		
	}
	
	protected List<DbMonitor> getMonitors(){
		return this.dataSource.getMonitors();
	}
	
	protected boolean isBroken(){
		return this.isBroken;
	}
	
	
	/**
	 * Checks if the connection is (logically) closed and throws an exception if it is.
	 * 
	 * @throws SQLException
	 *             on error
	 * 
	 * 
	 */
	private void checkClosed() throws SQLException {
		if (this.logicalClosed) {
			throw new SQLException("Connection is closed!");
		}
	}
	
	/**
	 * 以下是connection
	 */

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.connection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.connection.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		Statement result = null;
		checkClosed();
		try{
			result =new StatementWrapper(this.connection.createStatement(), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement result = null;
		checkClosed();
		try{
			result =new PreparedStatementWrapper(this.connection.prepareStatement(sql), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		CallableStatement result = null;
		checkClosed();
		try{
			result =new CallableStatementWrapper(this.connection.prepareCall(sql), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		String result = null;
		checkClosed();
		try{
			result = this.connection.nativeSQL(sql);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		checkClosed();
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		boolean result = false;
		checkClosed();
		try {
			result = this.connection.getAutoCommit();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void commit() throws SQLException {
		checkClosed();
		try {
			this.connection.commit();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public void rollback() throws SQLException {
		checkClosed();
		try {
			this.connection.rollback();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public void close() throws SQLException {
		try{
			if (this.logicalClosed)
				return;
			
			this.logicalClosed = true;
			this.dataSource.release(this);
			
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.logicalClosed || this.isBroken();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		DatabaseMetaData result = null;
		checkClosed();
		try {
			result = this.connection.getMetaData();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		checkClosed();
		try {
			this.connection.setReadOnly(readOnly);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		boolean result = false;
		checkClosed();
		try {
			result = this.connection.isReadOnly();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		checkClosed();
		try {
			this.connection.setCatalog(catalog);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public String getCatalog() throws SQLException {
		String result = null;
		checkClosed();
		try {
			result = this.connection.getCatalog();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		checkClosed();
		try {
			this.connection.setTransactionIsolation(level);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		int result = 0;
		checkClosed();
		try {
			result = this.connection.getTransactionIsolation();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}
	
	@Override
	public SQLWarning getWarnings() throws SQLException {
		SQLWarning result = null;
		checkClosed();
		try {
			result = this.connection.getWarnings();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void clearWarnings() throws SQLException {
		checkClosed();
		try {
			this.connection.clearWarnings();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Statement result = null;
		checkClosed();
		try {
			result = new StatementWrapper(this.connection.createStatement(resultSetType, resultSetConcurrency), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		PreparedStatement result = null;
		checkClosed();
		try {
			result = new PreparedStatementWrapper(this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		CallableStatement result = null;
		checkClosed();
		try {
			result = new CallableStatementWrapper(this.connection.prepareCall(sql, resultSetType, resultSetConcurrency), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		Map<String, Class<?>> result = null;
		checkClosed();
		try {
			result = this.connection.getTypeMap();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		checkClosed();
		try {
			this.connection.setTypeMap(map);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}
	
	@Override
	public void setHoldability(int holdability) throws SQLException {
		checkClosed();
		try {
			this.connection.setHoldability(holdability);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public int getHoldability() throws SQLException {
		int result = 0;
		checkClosed();
		try {
			result = this.connection.getHoldability();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		checkClosed();
		Savepoint result = null;
		try {
			result = this.connection.setSavepoint();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		checkClosed();
		Savepoint result = null;
		try {
			result = this.connection.setSavepoint(name);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		checkClosed();
		try {
			this.connection.rollback(savepoint);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		checkClosed();
		try {
			this.connection.releaseSavepoint(savepoint);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);

		}
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Statement result = null;
		checkClosed();
		try {
			result = new StatementWrapper(this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		PreparedStatement result = null;
		checkClosed();
		try {
			result = new PreparedStatementWrapper(this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		CallableStatement result = null;
		checkClosed();
		try {
			result = new CallableStatementWrapper(this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		PreparedStatement result = null;
		checkClosed();
		try {
			result = new PreparedStatementWrapper(this.connection.prepareStatement(sql,autoGeneratedKeys), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		PreparedStatement result = null;
		checkClosed();
		try {
			result = new PreparedStatementWrapper(this.connection.prepareStatement(sql,columnIndexes), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		PreparedStatement result = null;
		checkClosed();
		try {
			result = new PreparedStatementWrapper(this.connection.prepareStatement(sql,columnNames), this);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public Clob createClob() throws SQLException {
		Clob result = null;
		checkClosed();
		try {
			result = this.connection.createClob();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public Blob createBlob() throws SQLException {
		Blob result = null;
		checkClosed();
		try {
			result = this.connection.createBlob();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;

	}

	@Override
	public NClob createNClob() throws SQLException {
		NClob result = null;
		checkClosed();
		try {
			result = this.connection.createNClob();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		SQLXML result = null;
		checkClosed();
		try {
			result = this.connection.createSQLXML();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		boolean result = false;
		checkClosed();
		try {
			result = this.connection.isValid(timeout);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		this.connection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		this.connection.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		String result = null;
		checkClosed();
		try {
			result = this.connection.getClientInfo(name);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		Properties result = null;
		checkClosed();
		try {
			result = this.connection.getClientInfo();
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		Array result = null;
		checkClosed();
		try {
			result = this.connection.createArrayOf(typeName, elements);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}

		return result;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		Struct result = null;
		checkClosed();
		try {
			result = this.connection.createStruct(typeName, attributes);
		} catch (Throwable t) {
			throw markPossiblyBroken(t);
		}
		return result;
	}


    @Override
    public void setSchema(String setSchema)
            throws SQLException {
        checkClosed();
        try {
            this.connection.setSchema(setSchema);
        } catch (Throwable t) {
            throw markPossiblyBroken(t);
        }
    }

    @Override
    public String getSchema()
            throws SQLException {
        checkClosed();
        String result = null;
        try {
            result=this.connection.getSchema();
        } catch (Throwable t) {
            throw markPossiblyBroken(t);
        }
        return result;
    }



    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds)
            throws SQLException {
        checkClosed();
        try {
            this.connection.setNetworkTimeout(executor,milliseconds);
        } catch (Throwable t) {
            throw markPossiblyBroken(t);
        }
    }

    @Override
    public void abort(Executor abort)
            throws SQLException {
        checkClosed();
        try {
            this.connection.abort(abort);
        } catch (Throwable t) {
            throw markPossiblyBroken(t);
        }
    }

    @Override
    public int getNetworkTimeout()
            throws SQLException {
        checkClosed();
        int result = 0;
        try {
            result=this.connection.getNetworkTimeout();
        } catch (Throwable t) {
            throw markPossiblyBroken(t);
        }
        return result;
    }
}
