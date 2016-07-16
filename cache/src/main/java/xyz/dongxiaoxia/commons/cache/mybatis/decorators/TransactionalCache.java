package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 15:36
 */
public class TransactionalCache implements Cache {
    private Cache delegate;
    private boolean clearOncommit;
    private Map<String, AddEntry> entriesToAddOnCommit;
    private Map<String, RemoveEntry> entriesToRemoveOnCommit;

    public TransactionalCache(Cache delegate) {
        this.delegate = delegate;
        this.clearOncommit = false;
        this.entriesToAddOnCommit = new HashMap<>();
        this.entriesToRemoveOnCommit = new HashMap<>();
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public void putObject(String key, Object value) {
        this.entriesToRemoveOnCommit.remove(key);
        this.entriesToAddOnCommit.put(key,new AddEntry(this.delegate,key,value));
    }

    @Override
    public Object getObject(String key) {
        return this.clearOncommit ? null : this.delegate.getObject(key);
    }

    @Override
    public Object removeObject(String key) {
        this.entriesToAddOnCommit.remove(key);
        this.entriesToRemoveOnCommit.put(key,new RemoveEntry(this.delegate,key));
        return this.delegate.getObject(key);
    }

    @Override
    public void clear() {
        this.reset();
        this.clearOncommit = true;
    }

    public void commit(){
        Iterator i$;
        if (this.clearOncommit){
            this.delegate.clear();
        }else{
            i$ = this.entriesToRemoveOnCommit.values().iterator();
            while (i$.hasNext()){
                RemoveEntry entry = (RemoveEntry) i$.next();
                entry.commit();
            }
        }
        i$ = this.entriesToAddOnCommit.values().iterator();
        while (i$.hasNext()){
            AddEntry entry = (AddEntry) i$.next();
            entry.commit();
        }
        this.reset();
    }

    public void rollback(){
        this.reset();
    }

    private void reset() {
        this.clearOncommit = false;
        this.entriesToAddOnCommit.clear();
        this.entriesToRemoveOnCommit.clear();
    }

    @Override
    public int getSize() {
        return this.delegate.getSize();
    }

    private static class AddEntry {
        private Cache cache;
        private String key;
        private Object value;

        public AddEntry(Cache cache, String key, Object value) {
            this.cache = cache;
            this.key = key;
            this.value = value;
        }

        public void commit() {
            this.cache.putObject(this.key, this.value);
        }
    }

    private static class RemoveEntry {
        private Cache cache;
        private String key;

        public RemoveEntry(Cache cache, String key) {
            this.cache = cache;
            this.key = key;
        }

        public void commit() {
            this.cache.removeObject(this.key);
        }
    }
}
