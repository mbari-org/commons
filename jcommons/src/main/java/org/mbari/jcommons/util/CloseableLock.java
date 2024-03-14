package org.mbari.jcommons.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A wrapper around a ReentrantLock that allows you to use it in a try-with-resources block.
 * {@code
 * var lock = new CloseableLock();
 * try (lock.lock()) {
 *    // do something
 * }
 * }
 * 
 * @see https://debugagent.com/relearning-java-thread-primitives
 */
public class CloseableLock implements AutoCloseable, Lock {
  private final Lock lock;

  public CloseableLock() {
      this.lock = new ReentrantLock();
  }

  public CloseableLock(boolean fair) {
      this.lock = new ReentrantLock(fair);
  }


  public Lock getLock() {
      return lock;
  }

  @Override
  public void close() throws Exception {
      lock.unlock();
  }

  public void lock() {
      lock.lock();
  }

  public void lockInterruptibly() throws InterruptedException {
      lock.lock();
  }

  public void unlock() {
      lock.unlock();
  }

  @Override
  public boolean tryLock() {
    return lock.tryLock();
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return lock.tryLock(time, unit);
  }

  @Override
  public Condition newCondition() {
    return lock.newCondition();
  }
}