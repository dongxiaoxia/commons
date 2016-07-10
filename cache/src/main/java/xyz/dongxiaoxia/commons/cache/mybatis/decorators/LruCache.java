package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU最近最少使用算法缓存类
 *
 * @author dongxiaoxia
 * @create 2016-07-08 14:34
 */
public class LruCache implements Cache {
    private final Cache delegate;
    private Map<String, Object> keyMap;
    private String eldestKey;

    public LruCache(Cache delegate) {
        this.delegate = delegate;
        this.setSize(1024);
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public void putObject(String key, Object value) {
        this.delegate.putObject(key, value);
        this.cycleKeyList(key);
    }

    @Override
    public Object getObject(String key) {
        this.keyMap.get(key);
        return this.delegate.getObject(key);
    }

    @Override
    public Object removeObject(String key) {
        return this.delegate.removeObject(key);
    }

    @Override
    public void clear() {
        this.delegate.clear();
        this.keyMap.clear();
    }

    @Override
    public int getSize() {
        return this.delegate.getSize();
    }

    public void setSize(final int size) {
        this.keyMap = new LinkedHashMap<String, Object>(size, 0.75F, true) {
            private static final long serialVersionUID = 4267176411845948333L;

            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                boolean tooBig = this.size() > size;
                if (tooBig) {
                    LruCache.this.eldestKey = eldest.getKey();
                }
                return tooBig;
            }
        };
    }

    private void cycleKeyList(String key) {
        this.keyMap.put(key, key);
        if (this.eldestKey != null) {
            this.delegate.removeObject(this.eldestKey);
            this.eldestKey = null;
        }
    }
}
