package xyz.dongxiaoxia.commons.cache;

import org.junit.Test;

/**
 * Created by dongxiaoxia on 2016/5/15.
 */
public class SimpleLRUCacheTest {

    @Test
    public void test(){
        SimpleLRUCache<String,Integer> cache = new SimpleLRUCache(10);
        for (int i = 0; i < 10; i++) {
            cache.put("k" + i, i);
        }
        System.out.println("all cache : " + cache);
        cache.put("key"+10,10);
        System.out.println("all cache : " + cache);
    }

}