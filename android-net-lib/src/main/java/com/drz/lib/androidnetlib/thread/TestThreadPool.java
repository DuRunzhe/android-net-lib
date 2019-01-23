package com.drz.lib.androidnetlib.thread;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class TestThreadPool {
    // 阻塞队列最大任务数量
    static final int BLOCKING_QUEUE_SIZE = 20;
    static final int THREAD_POOL_MAX_SIZE = 10;

    static final int THREAD_POOL_SIZE = 6;
    /**
     * 缓冲BaseRequest任务队列
     */
    static ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(
            TestThreadPool.BLOCKING_QUEUE_SIZE);

    private static TestThreadPool instance = null;
    /**
     * 线程池，目前是十个线程，
     */
    static AbstractExecutorService pool = new ThreadPoolExecutor(
            TestThreadPool.THREAD_POOL_SIZE,
            TestThreadPool.THREAD_POOL_MAX_SIZE, 15L, TimeUnit.SECONDS,
            TestThreadPool.blockingQueue,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static synchronized TestThreadPool getInstance() {
        if (TestThreadPool.instance == null) {
            TestThreadPool.instance = new TestThreadPool();
        }
        return TestThreadPool.instance;
    }

    public static void removeAllTask() {
        TestThreadPool.blockingQueue.clear();
    }

    public static void removeTaskFromQueue(final Object obj) {
        TestThreadPool.blockingQueue.remove(obj);
    }

    /**
     * 关闭，并等待任务执行完成，不接受新任务
     */
    public static void shutdown() {
        if (TestThreadPool.pool != null) {
            TestThreadPool.pool.shutdown();
        }
    }

    /**
     * 关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
     */
    public static void shutdownRightnow() {
        if (TestThreadPool.pool != null) {
            TestThreadPool.pool.shutdownNow();
            try {
                // 设置超时极短，强制关闭所有任务
                TestThreadPool.pool.awaitTermination(1,
                        TimeUnit.MICROSECONDS);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行任务
     *
     * @param r
     */
    public void execute(final Runnable r) {
        if (r != null) {
            try {
                TestThreadPool.pool.execute(r);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
