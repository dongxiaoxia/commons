package xyz.dongxiaoxia.commons.cache;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dongxiaoxia on 2016/5/15.
 */
public interface Cache<K, V> {
    String getName();

    V getValue();

    Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys);

    boolean isPresent(K key);

    void put(K key,V value);

    void invalidate(K key);

    void invalidateAll(Iterator<? extends K> keys);

    void invalidateAll();

    boolean isEmpty();

    int size();

    void clear();

    Map<? extends K,? extends V> asMap();
}
