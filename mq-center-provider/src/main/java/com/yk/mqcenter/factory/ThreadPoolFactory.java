package com.yk.mqcenter.factory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.yk.mqcenter.properties.RocketProperties;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ThreadPoolFactory {

    /**
     * producer线程池配置
     *
     * @param rocketProperties
     * @return
     */
    public static ThreadPoolExecutor createProducerThreadPoolExecutor(RocketProperties rocketProperties) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-producer-%d").build();
        Integer threadNums = rocketProperties.getProducerThreadNums();
        return new ThreadPoolExecutor(threadNums, threadNums, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(4096),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * callback线程池配置
     *
     * @param rocketProperties
     * @return
     */
    public static ThreadPoolExecutor createCallbackThreadPoolExecutor(RocketProperties rocketProperties) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-callback-%d").build();
        Integer threadNums = rocketProperties.getCallbackThreadNums();
        return new ThreadPoolExecutor(threadNums, threadNums, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(4096),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
