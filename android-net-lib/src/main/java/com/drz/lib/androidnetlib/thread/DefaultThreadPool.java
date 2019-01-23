package com.drz.lib.androidnetlib.thread;

import android.util.Log;

import com.drz.lib.androidnetlib.util.AppUtils;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程达到corePoolSize后，任务线程会被继续创建，然后放入任务队列，
 * 任务队列也放满之后，在正在运行的任务数量小于maxPoolSize之前，
 * 任务仍会被创建运行，超过这个最大值之后，再调用excute方法添加任务时就会抛出异常。
 * <p>
 * Created by Administrator on 2017/2/19 0019.
 */

public class DefaultThreadPool {
    private DefaultThreadPool() {
        Log.d("debug", "- - -corePoolSize:" + corePoolSize + ", BLOCKING_QUEUE_SIZE" + BLOCKING_QUEUE_SIZE);
    }

    // 阻塞队列最大任务数量
    static final int BLOCKING_QUEUE_SIZE = 256;

    static final int corePoolSize = AppUtils.getCpuCoreNumber() * 2;//线程池保存的线程数量。
    static final int maximumPoolSize = corePoolSize * 8;//线程的最大数量。
    static final long keepAlive = 30L;//当线程数量超过线程池保存的线程数量之后，空闲线程关闭前等待新任务的时间
    static final TimeUnit unit = TimeUnit.SECONDS;//超出 corePoolSize 大小后，线程空闲的时间到达给定时间后将会关闭的时间单位
    static final ArrayBlockingQueue<Runnable> requestQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE);
    static AbstractExecutorService threadPool = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAlive,
            unit,
            requestQueue,
            new ThreadPoolExecutor.DiscardOldestPolicy()//失败关闭最旧的任务任务（一般倒数队列长度个任务最终还会被执行）
//            new ThreadPoolExecutor.CallerRunsPolicy()//失败自动重试当前任务（这样重试会导致在主线程执行任务然后异常）
    );

    public static DefaultThreadPool getInstance() {
        Log.i("debug", "线程池保存的核心线程数量：corePoolSize=" + corePoolSize
                + " 线程的最大数量:maximumPoolSize=" + maximumPoolSize
                + " 阻塞队列最大任务数量:BLOCKING_QUEUE_SIZE=" + BLOCKING_QUEUE_SIZE);
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
                Log.e("debug", "- - - - - - - -执行线程异常：" + e.getMessage());
            }
        }
    }

    private static class PoolHolder {
        private static final DefaultThreadPool instance = new DefaultThreadPool();
    }
}
