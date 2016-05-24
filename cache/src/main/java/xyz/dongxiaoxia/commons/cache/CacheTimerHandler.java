package xyz.dongxiaoxia.commons.cache;

import xyz.dongxiaoxia.commons.logging.Logger;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存操作类，对缓存进行管理，清除方式采用Timer定时方式
 *
 * @author dongxiaoxia
 * @create 2016-05-22 23:29
 */
public class CacheTimerHandler {
    private static final long SECOND_TIME = 1000L;//默认过期时间为20秒
    private static final int DEFAULT_VALIDITY_TIME = 20;//默认过期时间为20秒
    private static final Timer timer;
    private static final ConcurrentHashMap<String, CacheEntry> map;

    static {
        timer = new Timer();
        map = new ConcurrentHashMap<>(new HashMap<>(1 << 18));
    }

    /**
     * 添加缓存对象
     *
     * @param key
     * @param ce
     */
    public static synchronized void addCache(String key, CacheEntry ce) {
        addCache(key, ce, DEFAULT_VALIDITY_TIME);
    }

    /**
     * 添加缓存对象
     *
     * @param key
     * @param ce
     * @param validityTime 有效时间
     */
    public static synchronized void addCache(String key, CacheEntry ce, int validityTime) {
        map.put(key, ce);
        //添加过期定期
        timer.schedule(new TimeoutTimerTask(key), validityTime * SECOND_TIME);
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @return
     */
    public static synchronized CacheEntry getCache(String key) {
        return map.get(key);
    }

    /**
     * 检查是否含有指定Key的缓存
     *
     * @param key
     * @return
     */
    public static synchronized boolean isConcurrent(String key) {
        return map.contains(key);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static synchronized void removeCache(String key) {
        map.remove(key);
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public static int getCacheSize() {
        return map.size();
    }

    public static synchronized void clearCache() {
        if (null != timer) {
            timer.cancel();
        }
        map.clear();
    }

    /**
     * 清除超时缓存定时服务类
     */
    static class TimeoutTimerTask extends TimerTask {

        private String key;

        public TimeoutTimerTask(String key){
            this.key = key;
        }

        @Override
        public void run() {
            CacheTimerHandler.removeCache(key);
            Logger.info("remove:"+key);
        }
    }


}
