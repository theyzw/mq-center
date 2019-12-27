package com.yk.mqcenter.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import com.yk.mqcenter.enums.MsgType;
import com.yk.mqcenter.properties.RocketProperties;
import com.yk.mqcenter.util.ApplicationContextUtil;
import com.yk.mqcenter.util.RedisUtil;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yzw
 * @date 2019/12/25 13:18
 */
public class ConsumerContainer implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RocketProperties rocketProperties;

    public ConsumerContainer(RocketProperties rocketProperties) {
        this.rocketProperties = rocketProperties;
    }

    @PostConstruct
    public void init() {
        RedisUtil redisUtil = ApplicationContextUtil.getBean(RedisUtil.class);
        // 每个MsgType一个ConsumerBean
        for (MsgType msgType : MsgType.values()) {
            Properties properties = rocketProperties.getMqConsumerProperties();
            properties.setProperty(PropertyKeyConst.GROUP_ID, msgType.getGroupId());
            ConsumerBean consumerBean = new ConsumerBean();
            consumerBean.setProperties(properties);

            Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
            Subscription subscription = new Subscription();
            subscription.setTopic(msgType.getTopic());

            switch (msgType) {
                case DELAYED:
                    subscriptionTable.put(subscription, new CommonMessageListener(redisUtil));
                    break;
                default:
                    break;
            }
            consumerBean.setSubscriptionTable(subscriptionTable);

            consumerBean.start();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
