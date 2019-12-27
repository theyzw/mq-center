package com.yk.mqcenter.factory;

import com.yk.mqcenter.core.MqBase;
import com.aliyun.openservices.ons.api.Message;

public class MessageFactory {

    private MessageFactory() {
    }

    public static Message createMessage(MqBase mqBase) {
        return new Message(mqBase.getTopic(), mqBase.getTag(), mqBase.getBody().getBytes());
    }

    //public static Message createMessage(OrderMessage orderMessage, byte[] body) {
    //	return new Message(orderMessage.topic(),
    //			orderMessage.tag(),
    //			body);
    //}
    //
    //public static Message createMessage(TransactionMessage transactionMessage, byte[] body) {
    //	return new Message(transactionMessage.topic(),
    //			transactionMessage.tag(),
    //			body);
    //}
}
