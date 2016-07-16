package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;

import java.util.LinkedList;

/**
 * 先进先出缓存
 *
 * @author dongxiaoxia
 * @create 2016-07-08 14:16
 */
public class FifoCache implements Cache {
    private final Cache delegate;
    private LinkedList<String> keyList;
    private int size;

    public FifoCache(Cache delegate) {
        this.delegate = delegate;
        this.keyList = new LinkedList<>();
        this.size = 1024;
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public void putObject(String key, Object value) {
        this.cycleKeyList(key);
        this.delegate.putObject(key,value);
    }

    @Override
    public Object getObject(String key) {
        return this.delegate.getObject(key);
    }

    @Override
    public Object removeObject(String key) {
        return this.delegate.removeObject(key);
    }

    @Override
    public void clear() {
        this.delegate.clear();
        this.keyList.clear();
    }

    @Override
    public int getSize() {
        return this.delegate.getSize();
    }

    public void setSize(int size){
        this.size = size;
    }

    private void cycleKeyList(String key){
        this.keyList.addLast(key);
        if (this.keyList.size()>this.size){
            String oldestKey = this.keyList.removeFirst();
            this.delegate.removeObject(oldestKey);
        }
    }
}
