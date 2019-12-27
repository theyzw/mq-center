package com.yk.mqcenter.producer;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.yk.mqcenter.annotation.CommonMessage;
import com.yk.mqcenter.consts.Consts;
import com.yk.mqcenter.core.MqBase;
import com.yk.mqcenter.factory.MessageFactory;
import com.yk.mqcenter.util.ApplicationContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProducerThread implements Runnable {

    private ProducerBean producerBean;
    private Object annotation;
    private MqBase mqBase;

    public ProducerThread(ProducerBean producerBean, Object annotation, MqBase mqBase) {
        this.producerBean = producerBean;
        this.annotation = annotation;
        this.mqBase = mqBase;
    }

    @Override
    public void run() {
        log.info("producer init thread name={}", Thread.currentThread().getName());
        if (annotation instanceof CommonMessage) {
            Message message = MessageFactory.createMessage(mqBase);

            if (mqBase.getDeliverTime() != null) {
                message.setStartDeliverTime(mqBase.getDeliverTime());
            }

            // 设置callback_key，用于接收到消息时回调
            Properties properties = new Properties();
            properties.setProperty(Consts.MQ_CALLBACK_KEY, mqBase.getCallbackKey());
            properties.setProperty(Consts.MQ_MSG_ID, mqBase.getMsgId());
            message.setUserProperties(properties);

            send((CommonMessage) annotation, message);
        }
    }

    /**
     * 发送方式
     *
     * @param commonMessage
     * @param message
     */
    private void send(CommonMessage commonMessage, Message message) {
        switch (commonMessage.sendType()) {
            case SYNC:
                producerBean.send(message);
                break;
            case ASYNC:
                producerBean.sendAsync(message, ApplicationContextUtil.getSendCallback(commonMessage.callback()));
                break;
            case ONE_WAY:
                producerBean.sendOneway(message);
                break;
            default:
                producerBean.send(message);
                break;
        }
    }
}
