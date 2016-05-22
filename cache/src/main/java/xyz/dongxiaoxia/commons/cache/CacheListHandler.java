package xyz.dongxiaoxia.commons.cache;

import xyz.dongxiaoxia.commons.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存操作类，对缓存进行管理，采用处理队列，定时循环的方式】
 *
 * @author dongxiaoxia
 * @create 2016-05-22 22:36
 */
public class CacheListHandler {
    private static final long SECOND_TIME = 1000;
    private static final ConcurrentHashMap<String,CacheEntry1> map;
    private static final List<CacheEntry1> tempList;

    static{
        tempList = new ArrayList<>();
        map = new ConcurrentHashMap<>(new HashMap<>(1<<18));
        new Thread(new TimeoutThread()).start();
    }

    /**
     * 添加缓存对象
     * @param key
     * @param ce
     */
    public static void addCache(String key,CacheEntry1 ce){
        addCache(key,ce,ce.getValidityTime());
    }

    /**
     * 添加缓存对象
     * @param key
     * @param ce
     * @param validityTime 有效时间
     */
    public static synchronized void addCache(String key,CacheEntry1 ce,int validityTime){
        ce.setTimeoutStamp(System.currentTimeMillis() + validityTime * SECOND_TIME);
        map.put(key,ce);
        //添加到过期处理队列
        tempList.add(ce);
    }

    /**
     * 获取缓存对象
     * @param key
     * @return
     */
    public static synchronized CacheEntry1 getCache(String key){
        return map.get(key);
    }

    /**
     * 检查是否含有指定key的缓存
     * @param key
     * @return
     */
    public static synchronized boolean isConcurrent(String key){
        return map.contains(key);
    }

    /**
     * 删除缓存
     * @param key
     */
    public static synchronized void removeCache(String key){
        map.remove(key);
    }

    /**
     * 获取缓存大小
     * @return
     */
    public static int getCacheSize(){
        return map.size();
    }

    /**
     * 清除全部缓存
     */
    public static synchronized void clearCache(){
        tempList.clear();
        map.clear();
    }

    static class TimeoutThread implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    checkTime();
                }catch (Exception e){
                    Logger.error(e);
                }
            }
        }

        /**
         * 过期缓存的具体处理方法
         */
        private void checkTime() throws InterruptedException {
            CacheEntry1  tce = null;
            long timeoutTime = 1000L;
            if (1>tempList.size()){
                Logger.info("过期队列空，开始轮询");
                timeoutTime = 1000L;
                Thread.sleep(timeoutTime);
                return;
            }
            tce = tempList.get(0);
            timeoutTime = tce.getTimeoutStamp() - System.currentTimeMillis();
            if (0 < timeoutTime){
                Thread.sleep(timeoutTime);
                return;
            }
            Logger.info("清除过期缓存："+tce.getCacheKey());
            //清除过期缓存和删除对应的缓存队列
            tempList.remove(tce);
            removeCache(tce.getCacheKey());
        }
    }
}
