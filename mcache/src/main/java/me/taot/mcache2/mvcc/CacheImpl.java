package me.taot.mcache2.mvcc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.taot.mcache2.Cache;
import me.taot.mcache2.CacheException;
import me.taot.mcache2.Key;

public class CacheImpl implements Cache {

    private final ConcurrentMap<Class, Region> regions = new ConcurrentHashMap<>();
    
    private final TransactionManager transactionManager = new TransactionManager();

    @Override
    public <T> T get(Class<T> clazz, Key key) {
       return (T) getRegion(clazz).get(key);
    }

    @Override
    public <T> void put(Key key, T value) {
        if (value == null) {
            throw new CacheException("null value is not allowed");
        }
        Class clazz = value.getClass();
        getRegion(clazz).put(key, value);
    }

    @Override
    public <T> void remove(Class<T> clazz, Key key) {
        getRegion(clazz).remove(key);
    }

    @Override
    public <T> void addIndex(Class<T> clazz, String index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long beginTransaction() {
        Transaction tx = transactionManager.beginTransaction();
        return tx.getId();
    }

    @Override
    public void commit() {
        transactionManager.commit();
    }
    
    @Override
    public void rollback() {
        // TODO
    }

    private Region getRegion(Class clazz) {
        Region r = regions.get(clazz);
        if (r == null) {
            Region newR = new Region(clazz, transactionManager);
            r = regions.putIfAbsent(clazz, newR);
            if (r == null) {
                r = newR;
            }
        }
        return r;
    }
}
