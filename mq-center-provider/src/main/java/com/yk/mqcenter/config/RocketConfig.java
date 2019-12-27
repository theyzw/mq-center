package com.yk.mqcenter.config;

import javax.annotation.Resource;

import com.yk.mqcenter.aspect.RocketAspect;
import com.yk.mqcenter.consumer.ConsumerContainer;
import com.yk.mqcenter.factory.ThreadPoolFactory;
import com.yk.mqcenter.properties.RocketProperties;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzw
 * @date 2019/12/23 16:12
 */
@Configuration
@AutoConfigureAfter({RocketProperties.class, AppConfig.class})
public class RocketConfig {

    @Resource
    private RocketProperties rocketProperties;

    @Bean
    @ConditionalOnMissingBean(ConsumerContainer.class)
    public ConsumerContainer consumerContainer() {
        return new ConsumerContainer(rocketProperties);
    }

    @Bean(destroyMethod = "shutdown")
    public ProducerBean producerBean() {
        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(rocketProperties.getMqProducerProperties());
        producerBean.start();
        producerBean.setCallbackExecutor(ThreadPoolFactory.createCallbackThreadPoolExecutor(rocketProperties));
        return producerBean;
    }

    @Bean
    @ConditionalOnMissingBean(RocketAspect.class)
    public RocketAspect rocketRespect(ProducerBean producerBean) {
        return new RocketAspect(producerBean, rocketProperties);
    }
}
