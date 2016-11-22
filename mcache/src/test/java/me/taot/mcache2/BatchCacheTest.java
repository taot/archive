package me.taot.mcache2;

import me.taot.mcache2.entity.SecurityPosition;
import org.junit.Assert;
import org.junit.Test;

public class BatchCacheTest {

    @Test
    public void test_many_objects_with_in_transaction() {
        final Cache cache = CacheFactory.create();
        cache.beginTransaction();
        try {
            // PUT
            System.out.println("test_many_objects_with_in_transaction:");
            long num = 1000000L;
            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = new SecurityPosition(i, i, 1, "GOOG" + i);
                cache.put(new Key(i), pos);
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  PUT " + num + " duration: " + duration + " ms");

            // GET
            start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = cache.get(SecurityPosition.class, new Key(i));
                Assert.assertNotNull(pos);
                Assert.assertEquals(i, (long) pos.getAccountId());
                Assert.assertEquals("GOOG" + i, pos.getSecurity());
            }
            duration = System.currentTimeMillis() - start;
            System.out.println("  GET " + num + " duration: " + duration + " ms");
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
    }

    @Test
    public void test_many_objects_cross_transactions() {
        final Cache cache = CacheFactory.create();
        System.out.println("test_many_objects_cross_transactions:");
        final long num = 1000000L;

        // PUT
        try {
            cache.beginTransaction();

            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = new SecurityPosition(i, i, 1, "GOOG" + i);
                cache.put(new Key(i), pos);
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  PUT " + num + " duration: " + duration + " ms");

            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }

        // GET
        try {
            cache.beginTransaction();
            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = cache.get(SecurityPosition.class, new Key(i));
                Assert.assertNotNull(pos);
                Assert.assertEquals(i, (long) pos.getAccountId());
                Assert.assertEquals("GOOG" + i, pos.getSecurity());
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  GET " + num + " duration: " + duration + " ms");
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }

        // UPDATE
        try {
            cache.beginTransaction();
            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = cache.get(SecurityPosition.class, new Key(i));
                pos.setAccountId(i + 1);
                pos.setSecurity("APPL" + i);
                cache.put(new Key(i), pos);
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  UPDATE " + num + " duration: " + duration + " ms");
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }

        // GET AFTER UPDATE
        try {
            cache.beginTransaction();
            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = cache.get(SecurityPosition.class, new Key(i));
                Assert.assertNotNull(pos);
                Assert.assertEquals(i + 1, (long) pos.getAccountId());
                Assert.assertEquals("APPL" + i, pos.getSecurity());
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  GET AFTER UPDATE " + num + " duration: " + duration + " ms");
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }

        // REMOVE
        try {
            cache.beginTransaction();
            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                cache.remove(SecurityPosition.class, new Key(i));
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  REMOVE " + num + " duration: " + duration + " ms");
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }

        // GET AFTER REMOVE
        try {
            cache.beginTransaction();
            long start = System.currentTimeMillis();
            for (long i = 0; i < num; i++) {
                SecurityPosition pos = cache.get(SecurityPosition.class, new Key(i));
                Assert.assertNull(pos);
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("  GET AFTER REMOVE " + num + " duration: " + duration + " ms");
            cache.commit();
        } catch (Throwable t) {
            cache.rollback();
            throw t;
        }
    }
}
