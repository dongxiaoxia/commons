package xyz.dongxiaoxia.commons.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于LinkHashMap的简单LRUCache类
 *
 * @author dongxiaoxia
 * @create 2016-05-15 20:08
 */
public class SimpleLRUCache<K,V> extends LinkedHashMap<K,V>{

    private static int MAX_SIZE;

    public SimpleLRUCache(int maxSize){
        super(16,0.75f,true);
        if (maxSize<0){
            throw new IllegalArgumentException();
        }
        MAX_SIZE = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }
}
