package com.yk.mqcenter.aspect;

import java.util.concurrent.ThreadPoolExecutor;

import com.yk.mqcenter.annotation.CommonMessage;
import com.yk.mqcenter.factory.ThreadPoolFactory;
import com.yk.mqcenter.properties.RocketProperties;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author yzw
 * @date 2019/12/23 16:13
 */
@Aspect
@Slf4j
public class RocketAspect {

    private ProducerBean producerBean;
    private ThreadPoolExecutor threadPoolExecutor;

    public RocketAspect(ProducerBean producerBean, RocketProperties rocketProperties) {
        this.producerBean = producerBean;
        //this.producerBean.setCallbackExecutor(ThreadPoolFactory.createCallbackThreadPoolExecutor(rocketProperties));
        this.threadPoolExecutor = ThreadPoolFactory.createProducerThreadPoolExecutor(rocketProperties);
    }

    @Pointcut("@annotation(com.yk.mqcenter.annotation.CommonMessage)")
    public void commonMessagePointcut() {
        log.debug("send common message");
    }

    @Around("commonMessagePointcut()")
    public Object commonMessageSend(ProceedingJoinPoint joinPoint) throws Throwable {
        return InterceptRocket.intercept(joinPoint, producerBean, threadPoolExecutor, CommonMessage.class);
    }

}
