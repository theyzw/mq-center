package com.yk.mqcenter.producer;

import com.yk.mqcenter.annotation.CommonMessage;
import com.yk.mqcenter.core.MqBase;
import com.yk.mqcenter.enums.MsgType;
import org.springframework.stereotype.Component;

/**
 * @author yzw
 * @date 2019/12/24 17:57
 */
@Component
public class RocketSender {

    /**
     * 发送普通消息和定时消息
     *
     * @param mqBase
     * @return
     */
    @CommonMessage(msgType = MsgType.DELAYED, sendType = SendType.ASYNC)
    public MqBase sendCommonMsg(MqBase mqBase) {
        //消息发送前处理
        return mqBase;
    }

}
