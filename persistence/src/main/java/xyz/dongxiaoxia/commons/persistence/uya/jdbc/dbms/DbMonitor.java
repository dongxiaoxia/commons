package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

/**
 * Interface to the hooking mechanism of a connection lifecycle. Applications
 * will generally want to extend {@link com.jolbox.bonecp.hooks.AbstractConnectionHook} instead to provide a
 * default implementation. Applications might also want to make use of
 * connection.setDebugHandle(...) to keep track of additional information on
 * each connection.
 * <p>
 * Since the class is eventually loaded via reflection, the implementation must
 * provide a public, no argument constructor.
 * <p>
 * Warning: Be careful to make sure that the hook methods are re-entrant and
 * thread-safe; do not rely on external state without appropriate locking, use appropriate
 * synchronization!
 *
 * @author wallacew
 */
public interface DbMonitor {


    void onBound(DbDataSource ds);

    void onShutDown(DbDataSource ds);

    /**
     * Called upon getting a new connection from the JDBC driver (and prior to
     * inserting into the pool). You may call connection.getInternalConnection() to obtain
     * a handle to the actual (unwrapped) connection obtained from the driver.
     *
     * @param connection Handle to the new connection
     */
    void onCreate(DbDataSource ds, ConnectionWrapper connection);


    /**
     * Called when the connection is about to be completely removed from the
     * pool. Careful with this hook; the connection might be marked as being
     * broken. Use connection.isPossiblyBroken() to determine if the connection
     * has triggered an exceptions at some point.
     *
     * @param connection
     */
    void onDestroy(DbDataSource ds, ConnectionWrapper connection);

    /**
     * Called when the connection is about to be returned to the pool.
     *
     * @param connection being returned to pool.
     */
    void onCheckIn(DbDataSource ds, ConnectionWrapper connection);

    /**
     * Called when the connection is extracted from the pool and about to be
     * given to the application.
     *
     * @param connection about to given to the app.
     */
    void onCheckOut(DbDataSource ds, ConnectionWrapper connection);


    /**
     * Called whenever an exceptions on a connection occurs. This exceptions may be a connection failure, a DB failure or a
     * non-fatal logical failure (eg Duplicate key exceptions).
     * <p>
     * <p>SQLSTATE Value
     * <p>Value	Meaning
     * <p>08001	The application requester is unable to establish the connection.
     * <p>08002	The connection already exists.
     * <p>08003	The connection does not exist.
     * <p>08004	The application server rejected establishment of the connection.
     * <p>08007	Transaction resolution unknown.
     * <p>08502	The CONNECT statement issued by an application process running with a SYNCPOINT of TWOPHASE has failed, because no transaction manager is available.
     * <p>08504	An error was encountered while processing the specified path rename configuration file.
     * <p>SQL Failure codes 08001 & 08007 indicate that the database is broken/died (and thus all remaining connections are killed off).
     * <p>Anything else will be taken as the connection (not the db) being broken.
     * <p>
     * <p>
     * Note: You may use pool.isConnectionHandleAlive(connection) to verify if the connection is in a usable state again.
     * Note 2: As in all interceptor hooks, this method may be called concurrently so any implementation must be thread-safe.
     *
     * @param connection The handle that triggered this error
     * @param state      the SQLState error code.
     * @param t          Exception that caused this failure.
     * @return Returning true means: when you eventually close off this connection, test to see if the connection is still
     * alive and discard it if not (this is the normal behaviour). Returning false pretends that the connection is still ok
     * when the connection is closed (your application will still receive the original exceptions that was thrown).
     */
    void onException(DbDataSource ds, ConnectionWrapper connection, Throwable t);

    /**
     * 当数据库连接出现断开异常，可能需要测试数据库是否活着
     *
     * @param ds
     * @param connection
     */
    void onBroken(DbDataSource ds, ConnectionWrapper connection);

    /**
     * 成功的一次数据库处理
     *
     * @param ds
     * @param connection
     */
    void onSuccess(DbDataSource ds, ConnectionWrapper connection);

}
