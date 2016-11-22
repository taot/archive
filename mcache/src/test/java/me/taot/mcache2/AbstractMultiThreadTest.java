package me.taot.mcache2;

abstract class AbstractMultiThreadTest {

    protected long startTime;

    protected void resetTimer() {
        startTime = System.currentTimeMillis();
    }

    protected void myPrintln(String s) {
        double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;
        System.out.println("Thread " + Thread.currentThread().getName() + " (" + elapsed + " sec): " + s);
    }

    protected void mySleep(int nSec) {
        myPrintln("Sleeping " + nSec + " sec");
        try {
            Thread.sleep(nSec * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
