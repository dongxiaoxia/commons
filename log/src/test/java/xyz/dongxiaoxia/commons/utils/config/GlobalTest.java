package xyz.dongxiaoxia.commons.utils.config;

import org.junit.Test;

/**
 * Created by chenzhihua on 2016/7/2.
 */
public class GlobalTest {

    @Test
    public void test(){
        System.out.println(Global.SHOW);
        System.out.println(Global.getConfig("abc"));
        System.out.println(Global.getConfig("cba"));
        System.out.println(Global.getConfig("sdf"));

    }
}