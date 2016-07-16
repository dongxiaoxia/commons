package xyz.dongxiaoxia.commons.cache;

import java.io.Serializable;

/**
 * @author dongxiaoxia
 * @create 2016-05-23 21:51
 */
public class CacheEntry<K, V> implements Cacheable<K>,Serializable{

    private static final long serialVersionUID = -3971709196436977492L;
    private static final long SECOND_TIME = 1000;
    private final int DEFAULT_VALIDITY_TIME = 60;//默认时间 20秒

    private long expirationTime;
    private K key;
    private V value;

    private CacheEntry(){
        this.expirationTime = System.currentTimeMillis() + DEFAULT_VALIDITY_TIME * SECOND_TIME;
    }

    public CacheEntry(K key, V value){
        this();
        this.key = key;
        this.value =  value;
    }

    public CacheEntry(K key, V value, int secondsToLive) {
        this.value = value;
        this.key = key;
        if (secondsToLive != 0) {
            expirationTime = System.currentTimeMillis() + secondsToLive * SECOND_TIME;
        } else {
            expirationTime = 0;////This means it lives forever!
        }
    }

    @Override
    public boolean isExpired() {
        if (expirationTime != 0) {
            if (System.currentTimeMillis() > expirationTime) {
              //  Logger.debug("CacheResultSet.isExpired: Expired from Cache!EXPIRED TIME:" + new Date(expirationTime).toString() + " CURRENT TIME:" + new Date().toString());
                return true;
            } else {
             //   Logger.debug("CacheResultSet.isExpired: Expired not from Cache!");
                return false;
            }
        } else {
            return false;//This means it lives forever!
        }
    }

    @Override
    public K getKey() {
        return key;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
