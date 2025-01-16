/*
 * Java ThreadPool
 * Thread pool is a collection of threads (workers), which are available to perform the submitted tasks.
 * Once completed, worker thread get back to Thread Pool and wait for the new task to be assigned.
 * Threads can be reused.
 * 
 * Advantages:
 * 1) Thread creation time can be saved:
 * Each time a thread is created, space is allocated to it and this takes time. With thread pool this can be avoided by reusing the thread.
 * 2) Overhead of managing the thread lifecycle can be removed: 
 * The different state of the thread includes complexity. Thread pool removes this complexity.
 * 3) Increases the performance:
 * More threads mean, more context wsitching time, using control over thread creation, excess context switching can be avoided.
 * 
 * ThreadPoolExecutor:
 * It helps to create a customizable thread pool.
 */

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutor {
    public ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            ThreadFactory threadFactory,
                            RejectedExecutionHandler handler) {};
}
/*
 * corePoolSize: the number of threads which are initially created, and keep in the pool, even if they are idle.
 * allowCoreThreadTimeOut: If this 'property' is set to true (false by default), idle thread kept alive till specfied time by 'keepAliveTime'.
 * keepAliveTime: thread which are idle, gets terminated after this time.
 * maxPoolSize: maximum number of threads allowed in the pool.
 *              If number of threads == corePoolSize and queue is also full, then new threads are created (till it is less than 'maxPoolSize')
 *              Excess thread will remain in the pool, this pool is not shutdown or if allowCoreThreadTimeOut set to true, then excess thread gets terminated after keepAliveTime.
 * timeUnit: timeUnit for keepAliveTime, whether milliseconds or second or hours etc.
 * BlockingQueue: Queue used to hold task, before they got picked by the worker thread.
 *              - Bounded queue: Queue with fixed capacity. Like: ArrayBlockingQueue.
 *              - Unbounded queue: Queue with no fixed capacity. Like: LinkedBlockingQueue.
 * ThreadFactory: Factory for creating new threads. ThreadPoolExecutor uses it to create new thread, this factory provides us an interface to:
 *              - Give custom thread name.
 *              - To give custom thread priority.
 *              - To set thread daemon flag etc.
 * RejectedExecutionHandler: Handler for tasks that can not be accepted by thread pool. Generally logging logic can be put here for debugging purposes.
 *                          - new ThreadPoolExecutor.AbortPolicy: throws RejectedExecutionException 
 *                          - new ThreadPoolExecutor.CallerRunsPolicy: Executed the rejected task in the caller thread (thread that attempted to submit the task)
 *                          - new ThreadPoolExecutor.DiscardPolicy: silently discard the rejected task, without throwing any exception.
 *                          - new ThreadPoolExecutor.DiscardOldestPolicy: Discard the oldest task in the queue to accomodate the new task.
 * 
 * Running state:
 * shutdown() : moves the thread pool to shotdown state and does not allow any new task to be taken up, although the existing tasks will be taken by the pool.
 * stop() : directly stops the thread pool and stop any existing task as well.
 */

 // Example
 // Thread pool size: min = 2, max = 4. Queue size = 2.

 public class Main {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MINUTES, 
                                                        new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectHandler()/*ThreadPoolExecutor.DiscardOldestPolicy()*/);
        for (int i=1;i<=4;i++) {
            threadPoolExecutor.submit(() -> {
                System.out.prinln("Task processed by: " + Thread.currentThread().getName());
            });
        }
        executor.shotdown();
    }
 }

 class CustomThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread th =  new Thread(r);
        th.setPriority(Thread.NORM_PRIORITY);
        th.setDaemon(false);
        return th;
    }
 }

 class CustomRejectHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("Task rejected:" + r.toString());
    }
 }

 /*
  * Max thread size = No. of CPU Core * (1+Request waiting time/processing time) -> not full proof, does not consider memory
  * Max number of active tasks = Task arrival rate * task execution time
  * take care of JVM memory as well. One thread takes around 5 MB.
  */