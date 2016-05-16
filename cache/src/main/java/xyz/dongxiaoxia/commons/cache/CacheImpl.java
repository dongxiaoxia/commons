package xyz.dongxiaoxia.commons.cache;

import java.util.Iterator;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-05-15 15:29
 */
public class CacheImpl<K, V> implements Cache<K, V> {
//    private  final  String name;
//    private final caching
    public String getName() {
        return null;
    }

    public V getValue() {
        return null;
    }

    public Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys) {
        return null;
    }

    public boolean isPresent(K key) {
        return false;
    }

    public void put(K key, V value) {

    }

    public void invalidate(K key) {

    }

    public void invalidateAll(Iterator<? extends K> keys) {

    }

    public void invalidateAll() {

    }

    public boolean isEmpty() {
        return false;
    }

    public int size() {
        return 0;
    }

    public void clear() {

    }

    public Map<? extends K, ? extends V> asMap() {
        return null;
    }
}
