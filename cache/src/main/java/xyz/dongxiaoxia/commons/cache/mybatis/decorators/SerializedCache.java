package xyz.dongxiaoxia.commons.cache.mybatis.decorators;

import xyz.dongxiaoxia.commons.cache.mybatis.Cache;
import xyz.dongxiaoxia.commons.cache.mybatis.CacheException;

import java.io.*;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 15:02
 */
public class SerializedCache implements Cache {
    private Cache delegate;

    public SerializedCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public void putObject(String key, Object value) {
        if (value != null && !(value instanceof Serializable)) {
            throw new CacheException("SharedCache failed to make a copy of a non-serializable object: " + value);
        } else {
            this.delegate.putObject(key, this.serialize((Serializable) value));
        }
    }

    @Override
    public Object getObject(String key) {
        Object value = this.delegate.getObject(key);
        return value == null ? null : this.deserialize((byte[]) value);
    }

    @Override
    public Object removeObject(String key) {
        return this.delegate.removeObject(key);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    @Override
    public int getSize() {
        return this.delegate.getSize();
    }

    private byte[] serialize(Serializable value){
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(e);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            return e.toByteArray();
        } catch (IOException e) {
            throw new CacheException("Error serializing object. Cause: " + e,e);
        }
    }

    private Serializable deserialize(byte[] value) {
        try {
            ByteArrayInputStream e = new ByteArrayInputStream(value);
            ObjectInputStream ois = new ObjectInputStream(e);
            Serializable result = (Serializable)ois.readObject();
            ois.close();
            return result;
        } catch (Exception var5) {
            throw new CacheException("Error deserializing object.  Cause: " + var5, var5);
        }
    }
}
