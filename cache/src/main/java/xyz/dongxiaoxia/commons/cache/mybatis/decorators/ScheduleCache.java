package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 14:52
 */
public class ScheduleCache implements Cache {
    private Cache delegate;
    protected long clearInterval;
    protected long lastClear;

    public ScheduleCache(Cache delegate) {
        this.delegate = delegate;
        this.clearInterval = 3600000L;
        this.lastClear = System.currentTimeMillis();
    }

    public void setClearInterval(long clearInterval) {
        this.clearInterval = clearInterval;
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public void putObject(String key, Object value) {
        this.clearWhenStale();
        this.delegate.putObject(key, value);
    }

    @Override
    public Object getObject(String key) {
        return this.clearWhenStale() ? null : this.delegate.getObject(key);
    }

    @Override
    public Object removeObject(String key) {
        this.clearWhenStale();
        return this.delegate.removeObject(key);
    }

    @Override
    public void clear() {
        this.lastClear = System.currentTimeMillis();
        this.delegate.clear();
    }

    @Override
    public int getSize() {
        this.clearWhenStale();
        return this.delegate.getSize();
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    private boolean clearWhenStale() {
        if (System.currentTimeMillis() - this.lastClear > this.clearInterval){
            this.clear();
            return true;
        }else {
            return false;
        }
    }
}
