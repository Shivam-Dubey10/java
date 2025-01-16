public class Main {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MINUTES, 
                                                        new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectHandler()/*ThreadPoolExecutor.DiscardOldestPolicy()*/);
        for (int i=1;i<=4;i++) {
            executor.submit(() -> {
                System.out.prinln("Task processed by: " + Thread.currentThread().getName());
            });
        }
        executor.shotdown();
    }
 }

 /*
  * Whenever we call the submit method it returns a Future Object.
  * So we can hold that object for monitoring purposes.
  * 
  * Future:
  * Interface which represents the result of the async task.
  * It allows to check if:
  * - Computation in complete.
  * - Get the result.
  * - Take care of any exceptions. etc.
  */

  // Example:
  public class Main {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, 
                                                        new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        
        Future<?> futureObj = threadPoolExecutor.submit(() -> {
            System.out.println("This is the task, which thread will execute");
        });
        System.out.println(futureObj.isDone());
        executor.shotdown();
    }
 }