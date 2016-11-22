package me.taot.mcache2;

import me.taot.mcache2.mvcc.CacheImpl;

public class CacheFactory {

    private CacheFactory() {
    }

    public static Cache create() {
        return new CacheImpl();
    }
}
