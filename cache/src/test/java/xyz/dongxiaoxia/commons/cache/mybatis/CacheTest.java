package xyz.dongxiaoxia.commons.cache.mybatis;

import org.junit.Test;
import xyz.dongxiaoxia.commons.cache.mybatis.decorators.LoggingCache;
import xyz.dongxiaoxia.commons.cache.mybatis.impl.PerpetualCache;

/**
 * Created by chenzhihua on 2016/7/8.
 */
public class CacheTest {

    @Test
    public void test(){
        Cache cache = new LoggingCache(new PerpetualCache("test"));
        System.out.println(cache.getId());
        for (int i = 0;i<100;i++){
            cache.putObject(String.valueOf(i),Math.random());
        }
        System.out.println(cache.getSize());
        System.out.println(cache.getObject("23"));
        System.out.println(cache.removeObject("23"));
        System.out.println(cache.getObject("23"));
        cache.clear();
        System.out.println(cache.getSize());
        System.out.println(cache.getObject("23"));
    }
}