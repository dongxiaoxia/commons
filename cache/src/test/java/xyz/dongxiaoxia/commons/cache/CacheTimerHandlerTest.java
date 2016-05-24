package xyz.dongxiaoxia.commons.cache;

/**
 * Created by chenzhihua on 2016/5/23.
 */
public class CacheTimerHandlerTest {
    public static void main(String[] args) {
        CacheEntry ce;
        for (int i = 0;i<1000;i++){
            ce = new CacheEntry(i+"",i);
            CacheListHandler.addCache(i+"",ce);
        }
    }

}