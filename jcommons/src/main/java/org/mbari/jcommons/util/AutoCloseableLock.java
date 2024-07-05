package org.mbari.jcommons.util;

import java.util.concurrent.locks.ReentrantLock;


public class AutoCloseableLock extends ReentrantLock implements AutoCloseable {

    public AutoCloseableLock lockAndGet()  {
        lock();
        return this;
    }

    @Override
    public void close() {
        unlock();
    }


}