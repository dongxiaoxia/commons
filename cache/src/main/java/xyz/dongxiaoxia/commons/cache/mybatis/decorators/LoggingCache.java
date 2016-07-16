package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;
import xyz.dongxiaoxia.commons.logging.Log;
import xyz.dongxiaoxia.commons.logging.Logger;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 13:44
 */
public class LoggingCache implements Cache {

    private Log log = Logger.getLogger(LoggingCache.class);
    private Cache delegate;
    protected int requests = 0;
    protected int hits = 0;

    public LoggingCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public void putObject(String key, Object value) {
        this.delegate.putObject(key,value);
    }

    @Override
    public Object getObject(String key) {
        ++this.requests;
        Object value = this.delegate.getObject(key);
        if (value != null){
            ++this.hits;
        }
        this.log.debug("Cache Hit Ratio ["+this.getId()+"]:" + this.getHitRadio());
        return value;
    }

    @Override
    public Object removeObject(String key) {
        return this.delegate.removeObject(key);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public int getSize() {
        return this.delegate.getSize();
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    private double getHitRadio(){
        return  (double) this.hits /(double)this.requests;
    }
}
