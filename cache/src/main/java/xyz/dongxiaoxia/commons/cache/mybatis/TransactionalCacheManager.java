package xyz.dongxiaoxia.commons.cache.mybatis;

import xyz.dongxiaoxia.commons.cache.mybatis.decorators.TransactionalCache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 15:52
 */
public class TransactionalCacheManager {
    private Map<Cache,TransactionalCache> transactionalCaches = new HashMap<>();

    public TransactionalCacheManager(){
    }

    public void clear(Cache cache){
        this.transactionalCaches.get(cache).clear();
    }

    public Object getObject(Cache cache,String key){
        return this.getTransactionalCached(cache).getObject(key);
    }

    public void putObject(Cache cache,String key,Object value){
        this.getTransactionalCached(cache).putObject(key,value);
    }

    public void commit(){
        Iterator i$ = this.transactionalCaches.values().iterator();
        while (i$.hasNext()){
            TransactionalCache txCache = (TransactionalCache) i$.next();
            txCache.commit();
        }
    }

    public void rollback(){
        Iterator i$ = this.transactionalCaches.values().iterator();
        while (i$.hasNext()){
            TransactionalCache txCache = (TransactionalCache) i$.next();
            txCache.rollback();
        }
    }

    private TransactionalCache getTransactionalCached(Cache cache){
        TransactionalCache txCache = this.transactionalCaches.get(cache);
        if (txCache == null){
            txCache = new TransactionalCache(cache);
            this.transactionalCaches.put(cache,txCache);
        }
        return txCache;
    }
}
