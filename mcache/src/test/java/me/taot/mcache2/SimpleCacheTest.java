package me.taot.mcache2;

import me.taot.mcache2.entity.SecurityPosition;
import org.junit.Assert;
import org.junit.Test;

public class SimpleCacheTest {

    @Test
    public void test_with_in_transaction() {
        
        final Cache cache = CacheFactory.create();
        cache.beginTransaction();
        try {
            // get no item from empty cache
            {
                SecurityPosition pos = cache.get(SecurityPosition.class, new Key(1L));
                Assert.assertNull(pos);
            }

            // get item added in current transaction
            {
                SecurityPosition pos = new SecurityPosition(1L, 2L, 3, "GOOG");
                cache.put(new Key(1L), pos);
                SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
                Assert.assertEquals(pos, fromCache);
            }
            
            // update item in current transaction
            {
                SecurityPosition pos = new SecurityPosition(1L, 3L, 4, "APPL");
                cache.put(new Key(1L), pos);
                SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
                Assert.assertEquals(pos, fromCache);
            }
            
            // update again
            {
                SecurityPosition pos = new SecurityPosition(1L, 4L, 5, "MSFT");
                cache.put(new Key(1L), pos);
                SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
                Assert.assertEquals(pos, fromCache);
            }
            
            // remove
            {
                cache.remove(SecurityPosition.class, new Key(1L));
                SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
                Assert.assertNull(fromCache);
            }

            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
    }
    
    @Test
    public void test_cross_transactions() {
        
        final Cache cache = CacheFactory.create();
        
        // transaction 1
        cache.beginTransaction();
        SecurityPosition pos1 = new SecurityPosition(1L, 2L, 3, "GOOG");
        try {
            cache.put(new Key(1L), pos1);
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
        
        // transaction 2
        cache.beginTransaction();
        try {
            // get
            SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
            Assert.assertEquals(pos1, fromCache);
            
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
        
        // transaction 3
        cache.beginTransaction();
        try {
            // remove
            cache.remove(SecurityPosition.class, new Key(1L));
            SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
            Assert.assertNull(fromCache);
            
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
        
        // transaction 4
        cache.beginTransaction();
        try {
            // get
            SecurityPosition fromCache = cache.get(SecurityPosition.class, new Key(1L));
            Assert.assertNull(fromCache);
            
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
    }
}
