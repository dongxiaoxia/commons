package xyz.dongxiaoxia.commons.cache;

import java.io.Serializable;

/**
 * @author dongxiaoxia
 * @create 2016-05-22 20:58
 */
public class CacheEntry1 implements Serializable {
    private static final long serialVersionUID = -3971709196436977492L;
    private final int DEFAULT_VALIDITY_TIME = 20;//默认时间 20秒、

    private String cacheKey;
    private Object cacheContext;
    private int validityTime;//有效期时长，单位:秒
    private long timeoutStamp;//过期时间戳

    private CacheEntry1() {
        this.timeoutStamp = System.currentTimeMillis() + DEFAULT_VALIDITY_TIME * 1000;
        this.validityTime = DEFAULT_VALIDITY_TIME;
    }

    public CacheEntry1(String cachekey, Object cacheContext) {
        this();
        this.cacheContext = cacheContext;
        this.cacheKey = cachekey;
    }

    public CacheEntry1(String cacheKey,Object cacheContext,long timeoutStamp){
        this(cacheKey,cacheContext);
        this.timeoutStamp = timeoutStamp;
    }

    public CacheEntry1(String cacheKey,Object cacheContext,int validityTime){
        this(cacheKey,cacheContext);
        this.validityTime = validityTime;
        this.timeoutStamp = System.currentTimeMillis() + validityTime * 1000;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getDEFAULT_VALIDITY_TIME() {
        return DEFAULT_VALIDITY_TIME;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Object getCacheContext() {
        return cacheContext;
    }

    public void setCacheContext(Object cacheContext) {
        this.cacheContext = cacheContext;
    }

    public int getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(int validityTime) {
        this.validityTime = validityTime;
    }

    public long getTimeoutStamp() {
        return timeoutStamp;
    }

    public void setTimeoutStamp(long timeoutStamp) {
        this.timeoutStamp = timeoutStamp;
    }
}