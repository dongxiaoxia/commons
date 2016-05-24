package xyz.dongxiaoxia.commons.cache;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dongxiaoxia on 2016/5/15.
 *
 * 缓存接口
 */
public interface Cache<K, V> {

    void put(CacheEntry<K,V> cacheEntry);

    void put(K k,V v);

    void put(K k,V v,int secondsToLive);

    CacheEntry<K,V> remove(K k);

    CacheEntry<K,V> get(K key);

    boolean isEmpty();

    int size();

    void clear();

    Map<? extends K,? extends V> asMap();

    Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys);
}
