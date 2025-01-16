class SharedResource {
    boolean isItemPresent = false;
    public synchronized void addItem() {
        isItemPresent = true;
        notifyAll();
    }
    public synchronized void consumeItem() {
        while (!isItemPresent) {
            try {
                wait();
            } catch (Exceptoion e) {
                //
            }
        }
        isItemPresent = false;
    }
}
public class MultiThreadingBasics {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        Runnable producer = new ProducerThread(sharedResource);
        Runnable consumer = new ConsumerThread(sharedResource);
        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);
        t1.start();
        t2.start(); 
    }
}
public class ProducerThread implements Runnable {
    SharedResource sharedResource;
    public ProducerThread(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }
    @Override
    public void run() {
        sharedResource.addItem();
    }
}
public class ConsumerThread implements Runnable {
    SharedResource sharedResource;
    public ConsumerThread(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }
    @Override
    public void run() {
        sharedResource.consumeItem();
    }
}