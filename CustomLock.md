synchronized = Monitor lock
It is placed on an object.

When only one thread should be allowed in the critical section synchronized will not work cuz it is on an object.
Then there is custom lock. There are 4 types of custom lock:
1) ReentrantLock Lock:

public class SharedResorce {
    // ReentrantLock lock = new ReentrantLock();
    public void producer(ReentrantLock lock) {
        try {
            lock.lock(); //aquire lock
            // logic
        } catch (Exception e) {

        } finally {
            lock.unlock(); //release lock
        }
    }
}
public class Main () {
    
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        SharedResource r1 = new SharedResource();
        Thread t1 = new Thread(() -> {
            r1.producer(lock);
        });
        SharedResource r2 = new SharedResource();
        Thread t2 = new Thread(() -> {
            r2.producer(lock);
        });
        t1.start();
        t2.start();
    }
}
In this example you can see that r1 and r2 are two different objects so synchronized would have failed, but ReentrantLock is working.

# two threads can put shared lock but they can only read. only one thread can put exclusive lock when there is no other lock, you can read and write both in this.

2) ReadWriteLock:
ReadLock = shared lock
WriteLock = exclusive lock

public class SharedResorce {
    boolean isAvailable = false;
    // ReentrantLock lock = new ReentrantLock();
    public void producer(ReadWriteLock lock) {
        try {
            lock.readLock().lock(); //aquire lock
            isAvailable = true;
        } catch (Exception e) {

        } finally {
            lock.readLock().unlock(); //release lock
        }
    }
    public void consume(ReadWriteLock lock) {
        try {
            lock.writeLock().lock();
            isAvailable = false;
        } catch(Exception e) {

        } finally {
            lock.writeLock().unlock();
        }
    }
}
public class Main () {
    
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        SharedResource r1 = new SharedResource();
        Thread t1 = new Thread(() -> {
            r1.producer(lock);
        });
        
        Thread t2 = new Thread(() -> {
            r1.producer(lock);
        });
        SharedResource r2 = new SharedResource();
        Thread t3 = new Thread(() -> {
            r2.consume(lock);
        });
        t1.start();
        t2.start();
        t3.start();
    }
}

here t1 and t2 can simultaneously read but t3 will need exclusive lock.
When to use: when reads are very high compared to write.

3) StampedLock: 
Read/Write capability and optimistic lock (no lock aquired).
- Read/write Lock functionality:
public class SharedResorce {
    boolean isAvailable = false;
    
    public void producer(StampedLock lock) {
        long stamp = lock.readLock(); //aquire lock, returns this because it also support optimistic lock so that it will store the state of the obj.
        try { 
            isAvailable = true;
            Thread.sleep(1000);
        } catch (Exception e) {

        } finally {
            lock.unlockRead(stamp); //release lock
        }
    }
    public void consume(StampedLock lock) {
        long stamp = lock.writeLock(); //aquire lock
        try {
            isAvailable = false;
        } catch(Exception e) {

        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
public class Main () {
    
    public static void main(String[] args) {
        StampedLock lock = new StampedLock();
        SharedResource r1 = new SharedResource();
        Thread t1 = new Thread(() -> {
            r1.producer(lock);
        });
        
        Thread t2 = new Thread(() -> {
            r1.producer(lock);
        });
        
        Thread t3 = new Thread(() -> {
            r1.consume(lock);
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
- Optimistic functionality:
public class SharedResorce {
    boolean isAvailable = false;
    int a = 10;
    
    public void producer(StampedLock lock) {
        long stamp = lock.tryOptimisticRead(); //aquire lock, returns this because it also support optimistic lock so that it will store the state of the obj.
        try { 
            isAvailable = true;
            a = 11;
            Thread.sleep(6000);
            if (lock.validate(stamp)) { // checks if any write operation has occured by the time the read has happened.
                // update
            } else {
                a = 10;
                // rollback
            }
        } catch (Exception e) {

        } // no unlock needed because no lock is put in the first place.
    }
    public void consume(StampedLock lock) {
        long stamp = lock.writeLock(); //aquire lock
        try {
            isAvailable = false;
            a = 9;
        } catch(Exception e) {

        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
public class Main () {
    
    public static void main(String[] args) {
        StampedLock lock = new StampedLock();
        SharedResource r1 = new SharedResource();
        Thread t1 = new Thread(() -> {
            r1.producer(lock);
        });
        
        Thread t2 = new Thread(() -> {
            r1.producer(lock);
        });
        
        Thread t3 = new Thread(() -> {
            r1.consume(lock);
        });
        t1.start();
        t2.start();
        t3.start();
    }
}

4) SemaphoreLock:
Allows a particular thread to go inside a critical section, but there is  permit value which will decide the number of threads which will go inside the critical section.
Semaphore lock = new Semaphore(2); // here the permit value is 2, meaning that 2 threads can go inside the critical section simultaneously. 

public class SharedResorce {
    boolean isAvailable = false;
    public void producer(Semaphore lock) {
        try {
            lock.acquire(); //aquire lock
            isAvailable = true;
        } catch (Exception e) {

        } finally {
            lock.release(); //release lock
        }
    }
    public void consume(Semaphore lock) {
        try {
            lock.aquire();
            isAvailable = false;
        } catch(Exception e) {

        } finally {
            lock.release();
        }
    }
}
public class Main () {
    
    public static void main(String[] args) {
        Semaphore lock = new Semaphore(2);
        SharedResource r1 = new SharedResource();
        Thread t1 = new Thread(() -> {
            r1.producer(lock);
        });
        
        Thread t2 = new Thread(() -> {
            r1.producer(lock);
        });
        SharedResource r2 = new SharedResource();
        Thread t3 = new Thread(() -> {
            r2.consume(lock);
        });
        t1.start();
        t2.start();
        t3.start();
    }
}


# wait and notify will not work with these locks, await() & signal() will work here which does the similar job as wait() and notify() respectively. These are conditions.
public class Example {
    boolean isAvailable = false;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    public void producer(Semaphore lock) {
        try {
            lock.lock();
            if (isAvailable) {
                condition.await();
            }
            isAvailable = true;
            condition.signal(); //signalAll() can be used to signal all.
        } catch (Exception e) {

        } finally {
            lock.unlock(); //release lock
        }
    }
}