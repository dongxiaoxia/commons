package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 15:21
 */
public class SynchronizedCache implements Cache {
    private Cache delegate;

    public SynchronizedCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public synchronized void putObject(String key, Object value) {
        this.delegate.putObject(key, value);
    }

    @Override
    public synchronized Object getObject(String key) {
        return this.delegate.getObject(key);
    }

    @Override
    public synchronized Object removeObject(String key) {
        return this.delegate.removeObject(key);
    }

    @Override
    public synchronized void clear() {
        this.delegate.clear();
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    @Override
    public synchronized int getSize() {
        return this.delegate.getSize();
    }
}
