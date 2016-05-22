package xyz.dongxiaoxia.commons.cache;

/**
 * Created by chenzhihua on 2016/5/23.
 */
public class CacheListHandlerTest {

    public static void main(String[] args) {
        CacheEntry1 ce;
        for (int i = 0;i<1000;i++){
            ce = new CacheEntry1(i+"",i);
            CacheListHandler.addCache(i+"",ce);
        }
    }

}