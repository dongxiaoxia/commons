package xyz.dongxiaoxia.commons.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache工具类
 * <p>
 * Created by dongxiaoxia on 2016/5/24.
 */
public class CacheUtils {

    private static final ConcurrentHashMap<String, Cache> cacheManager = new ConcurrentHashMap<>();
    private static final String SYS_CACHE = "sysCache";
    private static final int MAX_SIZE = 500;

    private CacheUtils(){}

    /**
     * 获取SYS_CACHE缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 写入SYS_CACHE缓存
     *
     * @param key
     * @return
     */
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * 写入SYS_CACHE缓存
     * @param key
     * @param value
     * @param secondsToLive 有效时间 单位：秒
     */
    public static void put(String key,Object value,int secondsToLive){
        put(SYS_CACHE,key,value,secondsToLive);
    }

    /**
     * 写入SYS_CACHE缓存
     * @param cacheEntry
     */
    public static void put(CacheEntry cacheEntry){
        put(SYS_CACHE,cacheEntry);
    }

    /**
     * 从SYS_CACHE缓存中移除
     *
     * @param key
     * @return
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 获取缓存
     *
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key) {
        CacheEntry entry = getCache(cacheName).get(key);
        return entry == null ? null : entry.getValue();
    }

    /**
     * 写入缓存
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value) {
        getCache(cacheName).put(key,value);
    }

    /**
     * 写入缓存
     * @param cacheName
     * @param key
     * @param value
     * @param secondsToLive 有效时间 单位:秒
     */
    public static void put(String cacheName,String key,Object value, int secondsToLive){
        getCache(cacheName).put(key,value,secondsToLive);
    }

    /**
     * 写入缓存
     * @param cacheName
     * @param cacheEntry
     */
    public static void put(String cacheName,CacheEntry cacheEntry){
        getCache(cacheName).put(cacheEntry);
    }

    /**
     * 从缓存中移除
     *
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    /**
     * 获取SYS_CACHE缓存
     * @return
     */
    public static Map<String,Object> asMap(){
        return asMap(SYS_CACHE);
    }

    /**
     * 根据缓存名称获取缓存集合
     * @param cacheName
     * @return
     */
    public static Map<String,Object> asMap(String cacheName){
        return getCache(cacheName).asMap();
    }

    /**
     * 清除SYS_CACHE缓存
     */
    public static void clear(){
        clear(SYS_CACHE);
    }

    /**
     * 清楚指定缓存
     * @param cacheName
     */
    public static void clear(String cacheName){
        getCache(cacheName).clear();
    }

    /**
     * 获取SYS_cache缓存大小
     * @return
     */
    public int size(){
        return size(SYS_CACHE);
    }


    /**
     * 获取缓存大小
     * @param cacheName
     * @return
     */
    public int size(String cacheName){
        return getCache(cacheName).size();
    }


    /**
     * 获得一个Cache，没有则创建一个。
     *
     * @param cacheName
     * @return
     */
    private static Cache getCache(String cacheName) {
        Cache cache = cacheManager.get(cacheName);//// TODO: 2016/5/24 怎么方便切换不同的缓存类
        if (cache == null) {
            cache = new CacheListHandler<String,Object>();
            cacheManager.put(cacheName, cache);
        }
        return cache;
    }

    public static ConcurrentHashMap getCacheManager() {
        return cacheManager;
    }

}
