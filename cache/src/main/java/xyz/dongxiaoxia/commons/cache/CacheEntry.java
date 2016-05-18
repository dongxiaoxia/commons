package xyz.dongxiaoxia.commons.cache;

/**
 * 缓存实体
 *
 * @author dongxiaoxia
 * @create 2016-05-18 22:06
 */
public class CacheEntry {
    private String key;
    private Object value;
    private long timeOut;//更新时间
    private boolean expired;//终止时间

    public CacheEntry() {

    }


    public CacheEntry(String key, Object value, long timeOut, boolean expired) {

        this.key = key;
        this.value = value;
        this.timeOut = timeOut;
        this.expired = expired;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }


}
