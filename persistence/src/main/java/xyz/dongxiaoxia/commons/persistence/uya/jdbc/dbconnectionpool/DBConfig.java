package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

/**
 * 数据库配置
 *
 * @author dongxiaoxia
 * @create 2016-07-11 16:00
 */
public class DBConfig {
    private String driver;
    private String url;
    private String username;
    private String password;
    private int maxPoolSize;
    private int minPoolSize;
    private int idleTimeOut;
    private long timeout;
    private Boolean autoShrink;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getIdleTimeOut() {
        return idleTimeOut;
    }

    public void setIdleTimeOut(int idleTimeOut) {
        this.idleTimeOut = idleTimeOut;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Boolean getAutoShrink() {
        return autoShrink;
    }

    public void setAutoShrink(Boolean autoShrink) {
        this.autoShrink = autoShrink;
    }

}
