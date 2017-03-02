package com.drz.lib.androidnetlib.thread;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class DefaultThreadPool {
    private DefaultThreadPool() {
    }

    // 阻塞队列最大任务数量
    static final int BLOCKING_QUEUE_SIZE = 16;

    static final int corePoolSize = 2;//线程池保存的线程数量。
    static final int maximumPoolSize = 4;//线程的最大数量。
    static final long keepAlive = 15L;//当线程数量超过线程池保存的线程数量之后，空闲线程关闭前等待新任务的时间
    static final TimeUnit unit = TimeUnit.SECONDS;//超出 corePoolSize 大小后，线程空闲的时间到达给定时间后将会关闭的时间单位
    static final ArrayBlockingQueue<Runnable> requestQueue = new ArrayBlockingQueue<Runnable>(BLOCKING_QUEUE_SIZE);
    static AbstractExecutorService threadPool = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAlive,
            unit,
            requestQueue,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static DefaultThreadPool getInstance() {
        return PoolHolder.instance;
    }

    /**
     * 等待任务队列的任务执行结束后关闭
     */
    public void shutDownAfterExecuted() {
        if (!threadPool.isShutdown()) {
            threadPool.shutdown();
        }
    }

    /**
     * 关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
     */
    public void shutdownRightnow() {
        if (threadPool != null) {
            DefaultThreadPool.threadPool.shutdownNow();
            try {
                // 设置超时极短，强制关闭所有任务
                threadPool.awaitTermination(1,
                        TimeUnit.MICROSECONDS);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable runnable) {
        if (runnable != null) {
            try {
                threadPool.execute(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class PoolHolder {
        private static final DefaultThreadPool instance = new DefaultThreadPool();
    }
}
