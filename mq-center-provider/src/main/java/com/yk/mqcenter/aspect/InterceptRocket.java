package com.yk.mqcenter.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

import com.yk.mqcenter.core.MqBase;
import com.yk.mqcenter.factory.ProducerFactory;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author yzw
 * @date 2019/12/24 22:23
 */
@Slf4j
public class InterceptRocket {

    public static <T extends Annotation> Object intercept(ProceedingJoinPoint proceedingJoinPoint,
                                                          ProducerBean producerBean,
                                                          ThreadPoolExecutor threadPoolExecutor,
                                                          Class<T> annotationClass) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        T annotation = method.getAnnotation(annotationClass);
        Object proceed = proceedingJoinPoint.proceed();
        if (proceed == null) {
            return proceed;
        }
        MqBase mqBase = (MqBase) proceed;
        ProducerFactory.sendMsg(threadPoolExecutor, producerBean, annotation, mqBase);

        return proceed;
    }
}
