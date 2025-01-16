/*
Lock free based concurrency
In very specific use case conditions we can have lock free mechanisms for concurrency.

CAS Operation (Compare and Swap)
1) AtomicInteger
2) AtomicBoolean
3) AtomicLong
4) AtomicReference

CAS is also same as optimistic concurrency.
Low level operation. It is atomic. All modern processor supports it.

Involves 3 main parameters:
1) Memory location: location where variable is stored.
2) Expected value: value which should be present in the memory. (compare)
3) New value: value to be written to memory, if the current value matches the expected value. (update)

What does atomic means? = means single or nothing
Atomic can be used when we have read, modify and update.
*/
public class SharedResource {
    AtomicInteger counter = new AtomicInteger(0);

    public void increment() {
        counter.incrementAndGet();
    }

    public int get() {
        return counter.get();
    }
}
public class Main {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        Thread t1 = new Thread(() -> {
            for (int i=0;i<200;i++) {
                sharedResource.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i=0;i<200;i++) {
                sharedResource.increment();
            }
        });
        t1.start();
        t2.start();
    }
}