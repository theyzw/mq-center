package com.yk.mqcenter.factory;

import java.util.concurrent.ThreadPoolExecutor;

import com.yk.mqcenter.core.MqBase;
import com.yk.mqcenter.producer.ProducerThread;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzw
 * @date 2019/12/24 15:38
 */
@Slf4j
public class ProducerFactory {

    public static void sendMsg(ThreadPoolExecutor threadPoolExecutor,
                               ProducerBean producerBean,
                               Object annotation,
                               MqBase mqBase) {
        log.info("send msg:{}", mqBase);
        ProducerThread thread = new ProducerThread(producerBean, annotation, mqBase);
        threadPoolExecutor.execute(thread);
    }
}
