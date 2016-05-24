package xyz.dongxiaoxia.commons.cache;

import org.junit.Test;

/**
 * Created by Administrator on 2016/5/19.
 */
public class CacheManagerTest {

    public static void main(String[] args) throws InterruptedException {
        new CacheManagerTest().test();
    }

    @Test
    public void test() throws InterruptedException {
        System.out.println(CacheManager.getSimpleFlag("alksd"));
        CacheManager.put("abc", new CacheEntry("abc","abc",10));
        CacheManager.put("def", "123",10);
        CacheManager.put("ccc", "456",10);
        CacheManager.remove("");
        for (int i = 0; i < 10; i++) {
            CacheManager.put("" + i, new CacheEntry(i,i,i));
        }
        System.out.println("删除前的大小："+CacheManager.size());
        System.out.println( CacheManager.keySet());
        CacheManager.clear("abc");
        System.out.println("删除后的大小："+CacheManager.size());
        System.out.println("a："+CacheManager.size("a"));
        System.out.println(CacheManager.keySet());

        CacheEntry cacheEntry = new CacheEntry("111","222",10);
        CacheManager.put("111", cacheEntry,10);
        System.out.println(CacheManager.get("111").isExpired());
        Thread.sleep(100);
        System.out.println(CacheManager.get("111").isExpired());
        CacheManager.removeOutOfTimeCache(500);
       new Thread() {
           @Override
           public void run() {
               for (int i = 0;i<100;i++){
                   new Thread(){
                       @Override
                       public void run() {
                           for (int i = 0;i<100;i++){
                               CacheEntry entry = new CacheEntry("111","222",10);
                               CacheManager.put(String.valueOf(i),entry,i);
                               System.out.println("========================key:"+i+"=====================");
                           }
                       }
                   }.start();
               }
           }
       }.start();
    }
}