package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.DbConfig;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 提供一个具体数据源信息
 *
 * @author dongxiaoxia
 * @create 2016-07-12 15:13
 */
public class DbDataSource extends AbstractDataSource {
    
    private static org.slf4j.Logger log = LoggerFactory.getLogger(DbDataSource.class);

    public static int POOL_IS_FULL = 58001;

    private String name = "";

    private DbConfig config;

    private final List<DbMonitor> monitors = new ArrayList<>();

    private volatile boolean isAlive = true;

    /** set to true if the connection pool has been flagged as shutting down. */
    protected volatile boolean isShutDown;

    /** Connections available to be taken */
    private TransferQueue<ConnectionWrapper> freeConnections;

    /** Prevent repeated termination of all connections when the DB goes down. */
    protected Lock terminationLock = new ReentrantLock();

    /** Statistics lock. */
    protected ReentrantReadWriteLock statsLock = new ReentrantReadWriteLock();
    /** Number of connections that have been created. */
    private int size = 0;

    protected String checkLiveSQL = "SELECT 1";

    // 上次回收时间
    private volatile long lastReleaseTime = 0;
    // 回收间隔
    private  volatile static long releaseInterval = 10 * 60 * 1000;

    private static int releaseStrategyValve = 10;

    private AtomicLong creatingConnCount = new AtomicLong(0);
    private static int  MAX_CREATING_THREADS = 20;
    //	private final static int TOTAL_THREAD_SIZE = 100;
    private AtomicBoolean isInit = new AtomicBoolean(false);

    private final ReentrantLock releaseLock = new ReentrantLock();
    private final ExecutorService releaseExecutor = new ThreadPoolExecutor(1, 1, 1500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());


    public DbDataSource(DbConfig config, DbMonitor monitor) {
        releaseInterval = 9090L;
        this.config = config;
        freeConnections = new LinkedTransferQueue<ConnectionWrapper>();

        LoadDrivers();
        registerExcetEven();
        addMonitor(monitor);

        int min = config.getMinPoolSize();
        releaseInterval = config.getReleaseInterval() * 1000;
        releaseStrategyValve = config.getReleaseStrategyValue();
        min = min < 1 ? 1 : min;

        //初始化MAX_CREATING_THREADS 取所有线程数据的一半 ，最大连接数两者中的最小值
//		MAX_CREATING_THREADS = Math.min(TOTAL_THREAD_SIZE/2, config.getMaxPoolSize());

        //现在根据配置获取每个数据源的最大线程数
        if (!isInit.get()) {
//            MAX_CREATING_THREADS = MessageAlert.Factory.getMaxThreadsPerDs();// TODO: 2016/7/12  
            log.info("HaPlus : MAX_CREATING_THREADS is : " + MAX_CREATING_THREADS);
            isInit.set(true);
        }

        // 2011-05-30 保证启用最小值
        try {
            for (int index = 0; index < min; index++) {
                Connection connection = createConnection();
                connection.close();
            }
        } catch (Exception e) {
            log.warn("创建数据库连接失败"+e.getMessage());
            this.isAlive = false;
        }

    }

    public DbDataSource(DbConfig config) {
        this(config, null);
    }

    /**
     * shutdown the datasource
     */
    public synchronized void shutdown() {
        System.out.println("========================当前连接池关闭状态=========================="+this.isShutDown +"\n\n\n\n");
        if (this.isShutDown)
            return;

        log.info("Shutting down connection pool...");
        this.isShutDown = true;

        for (int index = 0; index < this.getMonitors().size(); index++) {
            DbMonitor monitor = this.getMonitors().get(index);
            monitor.onShutDown(this);
        }

        terminateAllConnections();
        log.info("Connection pool has been shutdown.");

    }

    /**
     * 判断数据库连接池是否满
     *
     * @return
     */
    public boolean isFull() {
        int min = config.getMinPoolSize();
        int max = config.getMaxPoolSize();

        min = min > 0 ? min : 1;
        max = max > min ? max : min;

        int totalSize = this.getSize();
        int freeSize = this.getAvailableConnections();

        return ((totalSize - freeSize) >= max) ? true : false;
    }

    /**
     * 断定数据库是活的
     *
     * @throws SQLException
     */
    private void assertLive() throws SQLException {
        if ((!isAlive) || isShutDown)
            throw new SQLException("SWAP " + this.getName() + " db connection pool is no alive or shutdown!");
    }

    @Override
    public Connection getConnection() throws SQLException {
        assertLive();

        ConnectionWrapper connection = this.freeConnections.poll();

        // create
        if (connection == null) {
//			synchronized (freeConnections) {
            if (isFull())
                throw new SQLException("SWAP db connection pool is full!" + this, null, POOL_IS_FULL); // vendorcode
            // 58001,
            // dbconnection
            // pool
            // is
            // full.

            // double check
            connection = this.freeConnections.poll();
            if (connection == null) {
                assertLive();
                connection = createConnection();
            }
        }
//		}
        connection.renew();

        // set readonly
        connection.setReadOnly(config.isReadonly());

        // hook calls
        for (int index = 0; index < this.getMonitors().size(); index++) {
            DbMonitor monitor = this.getMonitors().get(index);
            monitor.onCheckOut(this, connection);
        }

        return connection;
    }

    protected void release(ConnectionWrapper connection) throws SQLException {
        // hook calls
        for (int index = 0; index < this.getMonitors().size(); index++) {
            DbMonitor monitor = this.getMonitors().get(index);
            monitor.onCheckIn(this, connection);
        }
        log.debug("release Connection dataSource url:"+connection.getDataSource().getName()+"  isAlive:"+connection.getDataSource().isAlive());
        // // 池关闭或连接有严重错误，摧毁connection
        if (connection.isBroken() || isShutDown ) {
            // hook calls
            if (connection.isBroken()) {

                log.debug("SWAP connection.isBroken, pool is" + this);

                for (int index = 0; index < this.getMonitors().size(); index++) {
                    DbMonitor monitor = this.getMonitors().get(index);
                    monitor.onBroken(this, connection);
                }
            }

            destroyConnection(connection);
            return;
        }

        this.addFreeConnection(connection); // 放回池

        for (int index = 0; index < this.getMonitors().size(); index++) {
            DbMonitor monitor = this.getMonitors().get(index);
            monitor.onSuccess(this, connection);
        }

        keepSize();

        // executor.execute(keepSizeExecute);
    }

    final Runnable keepSizeExecute = new Runnable() {
        @Override
        public void run() {
            keepSize();
        }
    };

    public boolean isAlive() {
        return isAlive;
    }

    public void addMonitor(DbMonitor monitor) {
        if (monitor == null)
            return;
        if (!this.monitors.contains(monitor)) {
            this.monitors.add(monitor);
            monitor.onBound(this);
        }
    }

    public void removeMonitor(DbMonitor monitor) {
        this.monitors.remove(monitor);
    }

    public List<DbMonitor> getMonitors() {
        return monitors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 探测数据库是否活着多次
     * @return
     */
    public boolean checkDbLiveForTimes() {
        boolean rtn = true;
        int checkTimes = 0;
        try {
            rtn =  checkLive();
            while(!rtn && (checkTimes<10)){
                rtn =  checkLive();
                checkTimes++;
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("HaPlus: checkDbLiveForTimes Errors "+e.getMessage(),e);
            rtn = false;
        }
        log.debug("HaPlus: checkDbLiveForTimes name : rtn" + getName() +" : " +rtn);
        return rtn;
    }

    /**
     * 检查数据库是否活着
     *
     * @return
     */
    public boolean checkLive() {

        log.debug("SWAP BEGIN check DataSource live. NAME: " + name);
        Connection connection = null;
        boolean result = false;

        // first, try by a wrapped connection.
        try {
            connection = this.getConnection();
            if (connection != null) {
                result = checkConnectionAlive(connection);
            }
        } catch (SQLException e) {
            result = false;
        }

        DbUtil.closeConnection(connection);

        if (result) {
            this.isAlive = true;
            log.debug("SWAP END of check live with wrapped." + ", RESULT: " + isAlive + "; NAME: " + name
                    + "; Thread Id: " + Thread.currentThread().getId());
            return result;
        }

        // second try by a new raw connection.
        try {
            // safety， 新建一个原始连接
            connection = createConnection();
            result = checkConnectionAlive(connection);
        } catch (SQLException e) {
            result = false;
        }
        DbUtil.closeConnection(connection);
        this.isAlive = result;

        if (result)
            log.debug("SWAP END of check live with raw." + ", RESULT: " + isAlive + "; NAME: " + name
                    + "; Thread Id: " + Thread.currentThread().getId());
        else
            log.info("SWAP END of check live with raw." + ", RESULT: " + isAlive + "; NAME: " + name
                    + "; Thread Id: " + Thread.currentThread().getId());

        return result;
    }

    @Override
    public String toString() {
        int min = config.getMinPoolSize();
        int max = config.getMaxPoolSize();
        int totalSize = this.getSize();
        int freeSize = this.getAvailableConnections();
        return this.getName() + (this.isAlive ? " ALIVE" : " DEAD") + ", url: " + config.getConnectionURL() + ", min="
                + min + ", max=" + max + ", totalSize=" + totalSize + ", freeSize=" + freeSize;
    }

    protected void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
        if (!this.isAlive)
            this.terminateAllConnections();
    }

    /**
     * 保持数据库连接池的大小合适
     */
    protected void keepSize() {

        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastReleaseTime < releaseInterval)
            return;

        int min = config.getMinPoolSize();
        int max = config.getMaxPoolSize();

        min = min > 0 ? min : 1;
        max = max > min ? max : min;

        int totalSize = this.getSize();
        if (totalSize <= min)
            return;

        int freeSize = this.getAvailableConnections();

        // 保证线程池有2个
        if (freeSize <= 2)
            return;

        if(freeSize <= releaseStrategyValve){
            // 空闲连接与使用连接比例为2:1时可以收缩
            if (((freeSize * 3) > (totalSize * 2)) || totalSize > max) {
                if (currentTime - this.lastReleaseTime < releaseInterval)
                    return;
                this.lastReleaseTime = currentTime;
                log.debug("SWAP release a connection. pool is" + this);
                ConnectionWrapper connection = this.freeConnections.poll();
                if (connection == null)
                    return;
                try {
                    this.destroyConnection(connection);
                } catch (SQLException e) {
                    log.error("SWAP Error in keepSize to release connection", e);
                }
            }
        }else{

            final ReentrantLock lock = this.releaseLock;
            lock.lock();

            try{
                if (currentTime - this.lastReleaseTime < releaseInterval){
                    return;
                }
                this.lastReleaseTime = currentTime;
                log.debug("SWAP release "+ freeSize/2 +" connections. pool is" + this);
                //异步回收连接
                releaseExecutor.execute(new ReleaseTask(freeSize,this));
            }finally{
                lock.unlock();
            }



//			for(int i = 0 ; i < freeSize/2 ; i++){
//
//				ConnectionWrapper connection = this.freeConnections.poll();
//				if (connection == null)
//					return;
//				try {
//					this.destroyConnection(connection);
//				} catch (SQLException e) {
//					logger.error("SWAP Error in keepSize to release connection", e);
//				}
//			}
        }

    }

    /**
     * 检查一个链接是否有效
     *
     * @param connection
     * @return
     */
    protected boolean checkConnectionAlive(Connection connection) {
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(checkLiveSQL);
            result = true;
        } catch (SQLException e) {
            result = false;
        }

        DbUtil.closeResultSet(rs);
        DbUtil.closeStatement(stmt);
        return result;
    }

    /**
     * Adds a free connection.
     *
     * @param connection
     * @throws SQLException
     *             on error
     */
    protected void addFreeConnection(ConnectionWrapper connection) throws SQLException {
        if (!this.freeConnections.offer(connection)) {
            connection.internalClose();
        }
    }

    /**
     * 得到一个新连接
     *
     * @return
     * @throws SQLException
     */
    protected ConnectionWrapper createConnection() throws SQLException {

        log.debug("HaPlus: createConnection  cucrent creatingConnCount  :" + creatingConnCount.get());
        if (creatingConnCount.get() > MAX_CREATING_THREADS) {
            throw new SQLException("HaPlus: createConnection creatingConnCount is to max :" + creatingConnCount.get());

        }
        creatingConnCount.incrementAndGet();

        ConnectionWrapper lgconnection = null;

        try {

            Connection connection = null;
            String url = this.config.getConnectionURL();
            String username = this.config.getUsername();
            String password = this.config.getPassword();

            connection = DriverManager.getConnection(url, username, password);

            lgconnection = new ConnectionWrapper(this, connection);

            updateSize(1);

            // 2011-05-30
            this.lastReleaseTime = System.currentTimeMillis();

            // hook calls
            for (int index = 0; index < this.getMonitors().size(); index++) {
                DbMonitor monitor = this.getMonitors().get(index);
                monitor.onCreate(this, lgconnection);
            }

            log.debug("SWAP Created a new connection by " + this);
        } catch (SQLException e) {
            throw e;
        } finally {
            creatingConnCount.decrementAndGet();
        }


        return lgconnection;
    }

    /**
     * 完全摧毁一个数据库连接
     *
     * @param connection
     * @throws SQLException
     */
    protected void destroyConnection(ConnectionWrapper connection) throws SQLException {

        updateSize(-1);
        for (int index = 0; index < this.getMonitors().size(); index++) {
            DbMonitor monitor = this.getMonitors().get(index);
            monitor.onDestroy(this, connection);
        }

        connection.internalClose();
        log.debug("SWAP Destroyed a connection by " + this);

    }

    /**
     * Updates leased connections statistics
     *
     * @param increment
     *            value to add/subtract
     */
    protected void updateSize(int increment) {
        try {
            this.statsLock.writeLock().lock();
            this.size += increment;

        } finally {
            this.statsLock.writeLock().unlock();
        }
    }

    /**
     * @return the leasedConnections
     */
    protected int getSize() {
        try {
            this.statsLock.readLock().lock();
            return this.size;
        } finally {
            this.statsLock.readLock().unlock();
        }
    }

    private void LoadDrivers() {
        try {
            Driver driver = (Driver) Class.forName(config.getDriversClass()).newInstance();
            DriverManager.registerDriver(driver);
            DriverManager.setLoginTimeout(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Closes off all connections in all partitions. */
    protected void terminateAllConnections() {
        this.terminationLock.lock();
        try {
            ConnectionWrapper conn;
            while ((conn = this.freeConnections.poll()) != null) {
                try {
                    this.destroyConnection(conn);
                    // conn.internalClose();
                } catch (SQLException e) {
                    log.error("Error in attempting to close connection", e);
                }
            }
        } finally {
            this.terminationLock.unlock();
        }
    }

    /**
     * Returns the number of avail connections
     *
     * @return avail connections.
     */
    public int getAvailableConnections() {
        return this.freeConnections.size();
    }

    /*
     * when application exiting destroy all connections
     */
    private void registerExcetEven() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                shutdown();
            }
        });
    }

    public DbConfig getConfig() {
        return this.config;
    }


    class ReleaseTask implements Runnable {
        int freeSize;
        DbDataSource source;

        public ReleaseTask(int freeSize,DbDataSource source){
            this.freeSize = freeSize;
            this.source = source;
        }

        public void run() {
            for(int i = 0 ; i < freeSize/2 ; i++){

                ConnectionWrapper connection = source.freeConnections.poll();
                if (connection == null)
                    return;
                try {
                    source.destroyConnection(connection);
                } catch (SQLException e) {
                    log.error("SWAP Error in keepSize to release connection", e);
                }
            }
        }
    }

    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        return this.getParentLogger();
    }
}