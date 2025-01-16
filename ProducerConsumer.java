import java.util.List;

public class SharedResource {
    List<Integer> sharedBuffer;
    int bufferSize;
    public SharedResource (int bufferSize) {
        this.sharedBuffer = new LinkedList<>();
        this.bufferSize = bufferSize;
    }
    public synchronized void produce(int item) throws Exception {
        while (sharedBuffer.size() == sharedBuffer) {
            try {
                wait();
            } catch (Exception e) {
                //
            }
        }
        sharedBuffer.add(item);
        notify();
    }
    public synchronized int consume() throws Exception {
        while (sharedBuffer.isEmpty()) {
            try {
                wait();
            } catch (Exception e) {
                //
            }
        }
        int item = sharedBuffer.poll();
        notify();
        return item;
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource(3);
        Thread producer = new Thread(() -> {
            try {
                for (int i=0;i<6;i++) {
                    sharedResource.produce(i);
                }
            } catch (Exception e) {
                //
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i=0;i<6;i++) {
                    sharedResource.consume();
                }
            } catch (Exception e) {

            }
        });
        producer.start();
        consumer.start();
    }
}