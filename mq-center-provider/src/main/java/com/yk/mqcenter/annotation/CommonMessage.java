package com.yk.mqcenter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yk.mqcenter.enums.MsgType;
import com.yk.mqcenter.producer.DefaultSendCallback;
import com.yk.mqcenter.producer.SendType;
import com.aliyun.openservices.ons.api.SendCallback;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CommonMessage {

    /**
     * Message 所属的 Topic
     */
    MsgType msgType() default MsgType.DELAYED;

    /**
     * 订阅指定 Topic 下的 Tags：
     * 1. * 表示订阅所有消息
     * 2. TagA || TagB || TagC 表示订阅 TagA 或 TagB 或 TagC 的消息
     */
    String tag() default "*";

    /**
     * 重点：配合时间单位投递
     * 定时消息，单位毫秒（ms），在指定时间戳（当前时间之后）进行投递，例如 2016-03-07 16:21:00 投递。如果被设置成当前时间戳之前的某个时刻，消息将立刻投递给消费者。
     * 延时消息，单位毫秒（ms），在指定延迟时间（当前时间之后）进行投递，例如消息在 3 秒后投递
     */
    long startDeliverTime() default 0;

    /**
     * 消息发送类型 默认异步
     */
    SendType sendType() default SendType.ASYNC;

    /**
     * 自定义SendCallback类
     */
    Class<? extends SendCallback> callback() default DefaultSendCallback.class;
}
