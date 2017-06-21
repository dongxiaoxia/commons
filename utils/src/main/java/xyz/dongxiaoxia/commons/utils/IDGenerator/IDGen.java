package xyz.dongxiaoxia.commons.utils.IDGenerator;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类
 *
 * @author dongxiaoxia
 * @create 2016-07-06 16:54
 */
public class IDGen {
    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID。通过Random数字生成，中间无-分割。
     * @return
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 使用SecureRandom随机生成Long。
     * @return
     */
    public static long randomLong(){
        long result = Math.abs(random.nextLong());
        if (result < 0){
            return randomLong();
        }
        else
            return result;
    }

    public static void main(String[] args){
        System.out.println(IDGen.uuid());
        System.out.println(IDGen.uuid().length());
        System.out.println(IDGen.randomLong());
        for (int i = 0;i<1000;i++){
            System.out.println(IDGen.uuid());
        }
    }
}
