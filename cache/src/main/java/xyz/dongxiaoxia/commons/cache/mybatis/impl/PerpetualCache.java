package xyz.dongxiaoxia.commons.cache.mybatis.impl;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;
import xyz.dongxiaoxia.commons.cache.mybatis.CacheException;

import java.util.HashMap;
import java.util.Map;

/**
 * 永久缓存实现类
 *
 * @author dongxiaoxia
 * @create 2016-07-08 11:49
 */
public class PerpetualCache implements Cache{
    private String id;
    private Map<String,Object> cache = new HashMap<>();

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(String key, Object value) {
        this.cache.put(key,value);
    }

    @Override
    public Object getObject(String key) {
        return this.cache.get(key);
    }

    @Override
    public Object removeObject(String key) {
        return this.cache.remove(key);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public int getSize() {
        return this.cache.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getId() == null){
            throw new CacheException("Cache instances require an ID.");
        }else if (this == obj){
            return true;
        }else if (!(obj instanceof Cache)){
            return false;
        }else{
            Cache otherCache = (Cache)obj;
            return this.getId().equals(otherCache.getId());
        }
    }

    @Override
    public int hashCode() {
        if (this.getId() == null){
            throw new CacheException("Cache instances require an ID.");
        }else{
            return this.getId().hashCode();
        }
    }
}
