package xyz.dongxiaoxia.commons.cache;

import java.util.*;

/**
 * 缓存类
 *
 * @author dongxiaoxia
 * @create 2016-05-18 21:36
 */
//可扩展的功能：当chche到内存溢出时必须清除掉最早期的一些缓存对象，这就要求对每个缓存对象保存创建时间
public class CacheManager {
    private static HashMap cacheMap = new HashMap();
//    private static ConcurrentHashMap cacheMap = new ConcurrentHashMap();

    //单实例构造方法
    private CacheManager() {
        super();
    }

    //获取布尔值的缓存
    public static boolean getSimpleFlag(String key) {
        try {
            return (Boolean) cacheMap.get(key);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static long getServerStartdt(String key) {
        try {
            return (Long) cacheMap.get(key);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    //设置布尔值的缓存
    public synchronized static boolean setSimpleFlag(String key, boolean flag) {
        if (flag && getSimpleFlag(key)) {//假如为真不允许被覆盖
            return false;
        } else {
            cacheMap.put(key, flag);
            return true;
        }
    }

    public synchronized static boolean setSimpleFlag(String key, long serverStartTime) {
        if (cacheMap.get(key) == null) {
            cacheMap.put(key, serverStartTime);
            return true;
        } else {
            return false;
        }
    }

    //得到缓存，同步静态方法
    private synchronized static CacheEntry getCache(String key) {
        return (CacheEntry) cacheMap.get(key);
    }

    //判断是否存在一个缓存
    private synchronized static boolean hasCache(String key) {
        return cacheMap.containsKey(key);
    }

    //清除所有缓存
    public synchronized static void clear() {
        cacheMap.clear();
    }

    //清除某一类特定缓存。通过遍历HashMap下的所有对象，来判断它的KEY与传入的TYPE是否匹配
    public synchronized static void clear(String type) {
        Iterator it = cacheMap.entrySet().iterator();
        String key;
        ArrayList arr = new ArrayList();
        try {
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                key = (String) entry.getKey();
                if (key.startsWith(type)) {
                    arr.add(key);//如果匹配则删除掉
                }
            }
            for (int i = 0; i < arr.size(); i++) {
                remove((String) arr.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //清除指定缓存
    public static synchronized void remove(String key) {
        cacheMap.remove(key);
    }

    //载入缓存
    public synchronized static void put(String key, CacheEntry cacheEntry) {
        cacheMap.put(key, cacheEntry);
    }

    //获取缓存信息
    public static CacheEntry get(String key) {
        if (hasCache(key)) {
            CacheEntry cache = getCache(key);
            if (cacheExpired(cache)) {//调用判断是否终止方法
                cache.setExpired(true);
            }
            return cache;
        } else {
            return null;
        }
    }

    //载入缓存信息
    public synchronized static void put(String key, CacheEntry cacheEntry, long dt, boolean expired) {
        CacheEntry cache = new CacheEntry();
        cache.setKey(key);
        cache.setExpired(expired);//缓存默认载入时，终止状态为FALSE
        cache.setTimeOut(dt + System.currentTimeMillis());//设置多久后更新缓存
        cache.setValue(cacheEntry);
        cacheMap.put(key, cache);
    }

    //重写载入缓存信息方法
    public synchronized static void put(String key, CacheEntry cacheEntry, long dt) {
        CacheEntry cache = new CacheEntry();
        cache.setKey(key);
        cache.setValue(cacheEntry);
        cache.setTimeOut(dt + System.currentTimeMillis());
        cache.setExpired(false);
        cacheMap.put(key, cacheEntry);
    }

    //判断缓存是否终止
    public static boolean cacheExpired(CacheEntry cacheEntry) {
        if (null == cacheEntry) {
            return false;
        }
        long nowDt = System.currentTimeMillis();
        long cacheDt = cacheEntry.getTimeOut();
        if (cacheDt <= 0 || cacheDt > nowDt) {
            return false;
        } else {
            return true;
        }
    }

    //获取缓存中的大小
    public static int size() {
        return cacheMap.size();
    }

    //获取指定类型的大小
    public static int size(String type) {
        int k = 0;
        Iterator it = cacheMap.entrySet().iterator();
        String key;
        try {
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                key = (String) entry.getKey();
                if (key.indexOf(type) != -1) {
                    k++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return k;
    }

    //获取缓存对象中的所有键值名称
    public static Set<String> keySet() {
        return cacheMap.keySet();
    }

    public static Set<String> keySet(String type) {
        Set<String> keySet = cacheMap.keySet();
        Set<String> set = new HashSet<String>();
        for (String key :keySet){
            if (key.indexOf(type) != -1) {
                set.add(key);
            }
        }
        return set;
    }

    //定时删除缓存
    public synchronized static void removeOutOfTimeCache(long time){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("=====================================================================================");
                    System.out.println("============================执行定时删除过期缓存任务=================================");
                    Set<String> keySet = cacheMap.keySet();
                    String key;
                    CacheEntry cacheEntry;
                    List<String> removeKey = new ArrayList<>();
                    Iterator<String> it = keySet.iterator();
                    while (it.hasNext()){
                        key = it.next();
                        cacheEntry = (CacheEntry) cacheMap.get(key);
                        if (cacheExpired(cacheEntry)){
                            removeKey.add(key);
                            System.out.println("============================key:"+key+"=================================");
                        }
                    }
                    for (String s : removeKey){
                        cacheMap.remove(s);
                    }
                    System.out.println("============================执行定时删除过期缓存结束=================================");
                    System.out.println("=====================================================================================");
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

}
