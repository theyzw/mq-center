package com.yk.mqcenter.properties;

import java.util.Properties;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class RocketProperties {

    public Properties getMqProducerProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        properties.setProperty(PropertyKeyConst.INSTANCE_ID, this.instanceId);
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, String.valueOf(this.sendMsgTimeoutMillis));
        return properties;
    }

    public Properties getMqConsumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        properties.setProperty(PropertyKeyConst.INSTANCE_ID, this.instanceId);
        // 设置消费端线程数
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(this.consumeThreadNums));
        properties.setProperty(PropertyKeyConst.ConsumeTimeout, String.valueOf(this.consumeTimeout));
        return properties;
    }

    /**
     * 设置 TCP 协议接入点，从控制台获取
     */
    private String nameSrvAddr;

    /**
     * 您在阿里云账号管理控制台中创建的 AccessKey，用于身份认证
     */
    private String accessKey;

    /**
     * 您在阿里云账号管理控制台中创建的 SecretKey，用于身份认证
     */
    private String secretKey;

    /**
     * 阿里云instanceId
     */
    private String instanceId;


    /**
     * 设置消息发送的超时时间，单位（毫秒），默认：3000
     */
    private Integer sendMsgTimeoutMillis = 3000;

    /**
     * 设置事务消息第一次回查的最快时间，单位（秒）
     */
    private Integer checkImmunityTimeInSeconds = 5;

    /**
     * 设置 RocketMessage 实例的消费线程数，阿里云默认：20
     * 默认cpu数量*2+1
     */
    private Integer consumeThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;

    /**
     * 设置消息消费失败的最大重试次数，默认：16
     */
    private Integer maxReconsumeTimes = 16;

    /**
     * 设置每条消息消费的最大超时时间，超过设置时间则被视为消费失败，等下次重新投递再次消费。 每个业务需要设置一个合理的值，单位（分钟）。 默认：15
     */
    private Integer consumeTimeout = 15;

    /**
     * 只适用于顺序消息，设置消息消费失败的重试间隔时间默认100毫秒
     */
    private Integer suspendTimeMilli = 100;

    /**
     * 异步发送消息执行Callback的目标线程池
     */
    private Integer callbackThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;

    /**
     * 初始化生产者线程数，默认cpu数量*2+1
     */
    private Integer producerThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;

}
