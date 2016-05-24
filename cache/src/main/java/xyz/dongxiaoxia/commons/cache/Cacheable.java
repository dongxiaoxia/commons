package xyz.dongxiaoxia.commons.cache;

/**
 * Created by dongxiaoxia on 2016/5/23.
 */
public interface Cacheable<K> {

    boolean  isExpired();

    K getKey();
}
