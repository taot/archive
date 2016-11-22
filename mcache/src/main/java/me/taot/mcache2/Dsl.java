package me.taot.mcache2;

@Deprecated
public class Dsl {

    private Dsl() {
    }
    
    public static void transaction(Cache cache, Procedure p) {
        cache.beginTransaction();
        try {
            p.run(cache);
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
    }
    
    public static interface Procedure {
        void run(Cache cache);
    }
}
