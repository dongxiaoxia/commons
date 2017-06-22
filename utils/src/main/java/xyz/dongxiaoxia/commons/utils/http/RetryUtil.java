package xyz.dongxiaoxia.commons.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by dongxiaoxia on 2017/6/21.
 * 异常重试工具类
 */
public class RetryUtil {

    private static Logger logger = LoggerFactory.getLogger(RetryUtil.class);
    /**
     * 在遇到异常时尝试重试
     *
     * @param retryLimit    重试次数
     * @param retryCallable 重试回调
     * @param <V>           回调参数类型
     * @return 回调结果
     */
    public static <V> V retryOnException(int retryLimit, Callable<V> retryCallable) {
        for (int i = 0; i < retryLimit; i++) {
            try {
                return retryCallable.call();
            } catch (Exception e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("retry on %s times",i+1), e);
                }
            }
        }
        return null;
    }

    /**
     * 在遇到异常时尝试重试
     *
     * @param retryLimit    重试次数
     * @param sleepMillis   每次重试之后休眠的时间
     * @param retryCallable 重试回调
     * @param <V>           回调参数类型
     * @return 回调结果
     * @throws InterruptedException 睡眠时可能抛出异常
     */
    public static <V> V retryOnException(int retryLimit, long sleepMillis, Callable<V> retryCallable) throws InterruptedException {
        for (int i = 0; i < retryLimit; i++) {
            try {
                return retryCallable.call();
            } catch (Exception e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("retry on %s times",i+1), e);
                }
                Thread.sleep(sleepMillis);
            }
        }
        return null;
    }

    /**
     * 在遇到异常时尝试重试
     *
     * @param retryLimit    重试次数
     * @param sleepTme      每次重试之后休眠的时间
     * @param timeUnit      休眠时间单位
     * @param retryCallable 重试回调
     * @param <V>           回调参数类型
     * @return 回调结果
     * @throws InterruptedException 睡眠时可能抛出异常
     */
    public static <V> V retryOnException(int retryLimit, long sleepTme, TimeUnit timeUnit, Callable<V> retryCallable) throws InterruptedException {
        for (int i = 0; i < retryLimit; i++) {
            try {
                return retryCallable.call();
            } catch (Exception e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("retry on %s times",i+1), e);
                }
                timeUnit.sleep(sleepTme);
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        retryOnException(5,1,TimeUnit.SECONDS, () -> {
            System.out.println("++++++++++");
            int a = 1/0;
            return "ss";
        });
    }

}
