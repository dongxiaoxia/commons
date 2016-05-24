package xyz.dongxiaoxia.commons.cache;

import xyz.dongxiaoxia.commons.logging.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存操作类，对缓存进行管理，采用处理队列，定时循环的方式
 *
 * @author dongxiaoxia
 * @create 2016-05-22 22:36
 */
public class CacheListHandler<K,V> implements Cache<K,V> {
    private final ConcurrentHashMap<K, CacheEntry<K,V>> map;
    private final List<CacheEntry<K,V>> tempList;

    public CacheListHandler(){
        tempList = new ArrayList<>();
        map = new ConcurrentHashMap<>(new HashMap<>(1 << 18));
        new Thread(new TimeoutThread()).start();
    }

    /**
     * 添加缓存对象
     * @param key
     * @param value
     */
    @Override
    public synchronized void put(K key, V value) {
        CacheEntry<K,V> cacheEntry = new CacheEntry<>(key, value);
        put(cacheEntry);
    }

    /**
     * 添加缓存对象
     * @param key
     * @param value
     * @param secondsToLive 有效时间，单位：秒
     */
    @Override
    public synchronized void put(K key, V value, int secondsToLive) {
        CacheEntry<K,V> cacheEntry = new CacheEntry<>(key, value, secondsToLive);
        put(cacheEntry);
    }

    /**
     * 添加缓存对象
     * @param cacheEntry
     */
    @Override
    public synchronized void put(CacheEntry<K, V> cacheEntry) {
        if (cacheEntry == null || cacheEntry.getKey() == null) {
            throw new IllegalArgumentException("CacheEntry key can not be null!");
        }
        map.put(cacheEntry.getKey(), cacheEntry);
        //添加到过期处理队列
        tempList.add(cacheEntry);
    }

    /**D
     * 删除缓存
     * @param k
     * @return
     */
    @Override
    public synchronized CacheEntry<K, V> remove(K k) {
        return map.remove(k);
    }

    /**
     * 获取缓存对象
     * @param key
     * @return
     */
    @Override
    public CacheEntry<K,V> get(K key){
        CacheEntry<K,V> cacheEntry = map.get(key);
        if (cacheEntry == null) {
            return null;
        }
        if (cacheEntry.isExpired()) {
            map.remove(key);
            return null;
        } else {
            return cacheEntry;
        }
    }

    /**
     * 判断缓存是否为空
     * @return
     */
    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 获取缓存大小
     * @return  获取缓存大小
     */
    @Override
    public synchronized int size() {
        return map.size();
    }

    /**
     * 清除全部缓存
     */
    @Override
    public synchronized void clear() {
        tempList.clear();
        map.clear();
    }

    @Override
    public Map<? extends K, ? extends V> asMap() {
        return null;
    }

    @Override
    public Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys) {
        return null;
    }

    class TimeoutThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    checkTime();
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
        }

        /**
         * 过期缓存的具体处理方法
         */
        private void checkTime() throws InterruptedException {
            CacheEntry<K,V> tce;
            long timeoutTime = 1000L;
            if (1 > tempList.size()) {
                Logger.info("过期队列空，开始轮询");
                timeoutTime = 1000L;
                Thread.sleep(timeoutTime);
                return;
            }
            tce = tempList.get(0);
            if (!tce.isExpired()) {
                Thread.sleep(timeoutTime);
                return;
            }
            Logger.info("清除过期缓存：" + tce.getKey());
            //清除过期缓存和删除对应的缓存队列
            tempList.remove(tce);
            remove(tce.getKey());
        }
    }
}
