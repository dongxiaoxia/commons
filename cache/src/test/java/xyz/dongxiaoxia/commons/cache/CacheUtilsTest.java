package xyz.dongxiaoxia.commons.cache;

import java.util.Date;

/**
 * Created by chenzhihua on 2016/5/25.
 */
public class CacheUtilsTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i<100){
                    CacheUtils.put(i + "", new Date());
                    i++;
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i<100){
                    CacheUtils.put("userCache",i + "", new Date(),i);
                    i++;
                }
            }
        }).start();
    }

}