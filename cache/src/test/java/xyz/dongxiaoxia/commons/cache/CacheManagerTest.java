package xyz.dongxiaoxia.commons.cache;

import org.junit.Test;

/**
 * Created by Administrator on 2016/5/19.
 */
public class CacheManagerTest {

    @Test
    public void test() throws InterruptedException {
        System.out.println(CacheManager.getSimpleFlag("alksd"));
        CacheManager.put("abc", new CacheEntry());
        CacheManager.put("def", new CacheEntry());
        CacheManager.put("ccc", new CacheEntry());
        CacheManager.remove("");
        CacheEntry c = new CacheEntry();
        for (int i = 0; i < 10; i++) {
            CacheManager.put("" + i, c);
        }
        CacheManager.put("aaaaaaaa", c);
        CacheManager.put("abchcy;alskd", c);
        CacheManager.put("cccccccc", c);
        CacheManager.put("abcoqiwhcy", c);
        System.out.println("删除前的大小："+CacheManager.size());
        System.out.println( CacheManager.keySet());
        CacheManager.clear("aaaa");
        System.out.println("删除后的大小："+CacheManager.size());
        System.out.println("a："+CacheManager.size("a"));
        System.out.println(CacheManager.keySet());

        CacheEntry cacheEntry = new CacheEntry("111","222",10,false);
        CacheManager.put("111",cacheEntry,10,false);
        System.out.println(CacheManager.get("111").isExpired());
        Thread.sleep(100);
        System.out.println(CacheManager.get("111").isExpired());
    }
}