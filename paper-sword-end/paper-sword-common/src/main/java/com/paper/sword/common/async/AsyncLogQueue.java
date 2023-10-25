package com.paper.sword.common.async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class AsyncLogQueue {
    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int QUEUE_SIZE = 20;

    private static ExecutorService senderAsync = new ThreadPoolExecutor(
            THREAD_SIZE,
            THREAD_SIZE,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_SIZE),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Async_Log_Thread");
                    return thread;
                }
            },
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    log.error("Async_Log_Thread.....Error");
                }
            });

    public static void submit(Runnable runnable){
        senderAsync.submit(runnable);
    }
}
