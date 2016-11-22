package me.taot.mcache2;

import me.taot.mcache2.mvcc.TransactionManager;

public interface Cache {

    <T> T get(Class<T> clazz, Key key);

    <T> void put(Key key, T value);

    <T> void remove(Class<T> clazz, Key key);

    <T> void addIndex(Class<T> clazz, String index);

    long beginTransaction();

    void commit();
    
    void rollback();
}
