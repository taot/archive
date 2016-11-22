package me.taot.mcache2;

import me.taot.mcache2.entity.SecurityPosition;
import org.junit.Assert;
import org.junit.Test;

public class MultiThreadCacheTest extends AbstractMultiThreadTest {

    protected volatile boolean successful = true;

    @Test
    public void test_interleaved_transactions() {

        resetTimer();

        final Cache cache = CacheFactory.create();
        System.out.println("test_interleaved_transactions:");
        final long num = 100L;

        // Thread 1
        Thread t1 = new Thread("putter") {

            @Override
            public void run() {

                // PUT at 0 - 10 sec
                try {
                    cache.beginTransaction();

                    myPrintln("PUT " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition pos = new SecurityPosition(i, i, 1, "GOOG" + i);
                        cache.put(new Key(i), pos);
                    }
                    myPrintln("PUT " + num + " done");

                    mySleep(10);

                    cache.commit();
                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(10);

                // UPDATE at 20 - 30 sec
                try {
                    cache.beginTransaction();

                    myPrintln("UPDATE " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition pos = new SecurityPosition(i, i + 1, 2, "APPL" + i);
                        cache.put(new Key(i), pos);
                    }
                    myPrintln("UPDATE " + num + " done");

                    mySleep(10);

                    cache.commit();
                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(10);

                // DELETE at 40 - 50 sec
                try {
                    cache.beginTransaction();

                    myPrintln("DELETE " + num);
                    for (long i = 0; i < num; i++) {
                        cache.remove(SecurityPosition.class, new Key(i));
                    }
                    myPrintln("DELETE " + num + " done");

                    mySleep(10);

                    cache.commit();
                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }
            }
        };

        // Thread 2
        Thread t2 = new Thread("getter") {
            @Override
            public void run() {

                // GET at 5 - 10 sec
                try {
                    mySleep(5);
                    cache.beginTransaction();
                    myPrintln("GET " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition p = cache.get(SecurityPosition.class, new Key(i));
                        Assert.assertNull(p);
                    }
                    myPrintln("GET " + num + " done");
                    cache.commit();

                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(10);

                // GET at 15 - 20 sec
                try {
                    // First attempt
                    cache.beginTransaction();
                    myPrintln("GET " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition p = cache.get(SecurityPosition.class, new Key(i));
                        Assert.assertNotNull(p);
                        Assert.assertEquals(i, (long) p.getAccountId());
                        Assert.assertEquals(1, p.getLedgerId());
                        Assert.assertEquals("GOOG" + i, p.getSecurity());
                    }
                    myPrintln("GET " + num + " done");
                    mySleep(5);
                    cache.commit();

                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(5);

                // GET at 25 - 30 sec
                try {
                    cache.beginTransaction();
                    myPrintln("GET " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition p = cache.get(SecurityPosition.class, new Key(i));
                        Assert.assertNotNull(p);
                        Assert.assertEquals(i, (long) p.getAccountId());
                        Assert.assertEquals(1, p.getLedgerId());
                        Assert.assertEquals("GOOG" + i, p.getSecurity());
                    }
                    myPrintln("GET " + num + " done");
                    mySleep(5);
                    cache.commit();

                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(5);

                // GET at 35 - 40 sec
                try {
                    cache.beginTransaction();
                    myPrintln("GET " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition p = cache.get(SecurityPosition.class, new Key(i));
                        Assert.assertNotNull(p);
                        Assert.assertEquals(i + 1, (long) p.getAccountId());
                        Assert.assertEquals(2, p.getLedgerId());
                        Assert.assertEquals("APPL" + i, p.getSecurity());
                    }
                    myPrintln("GET " + num + " done");
                    mySleep(5);
                    cache.commit();

                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(5);

                // GET at 45 - 50 sec
                try {
                    // First attempt
                    cache.beginTransaction();
                    myPrintln("GET " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition p = cache.get(SecurityPosition.class, new Key(i));
                        Assert.assertNotNull(p);
                        Assert.assertEquals(i + 1, (long) p.getAccountId());
                        Assert.assertEquals(2, p.getLedgerId());
                        Assert.assertEquals("APPL" + i, p.getSecurity());
                    }
                    mySleep(5);
                    cache.commit();

                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }

                mySleep(5);

                // GET at 55 - 60 sec
                try {
                    // First attempt
                    cache.beginTransaction();
                    myPrintln("GET " + num);
                    for (long i = 0; i < num; i++) {
                        SecurityPosition p = cache.get(SecurityPosition.class, new Key(i));
                        Assert.assertNull(p);
                    }
                    mySleep(5);
                    cache.commit();

                } catch (Throwable t) {
                    cache.rollback();
                    successful = false;
                    throw t;
                }
            }

        };

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end");

        Assert.assertTrue(successful);
    }


}
