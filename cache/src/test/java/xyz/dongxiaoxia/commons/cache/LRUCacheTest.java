package xyz.dongxiaoxia.commons.cache;

import org.junit.Test;

/**
 * Created by chenzhihua on 2016/5/15.
 */
public class LRUCacheTest {

    @Test
    public void test() {
        LRUCache lCache = new LRUCache(2);
        lCache.set(2, 1);
        lCache.set(1, 1);
        lCache.set(2, 3);
        lCache.set(4, 1);
        System.out.println(lCache.get(1));
        System.out.println(lCache.get(2));
        System.out.println(lCache.get(4));
    }
}