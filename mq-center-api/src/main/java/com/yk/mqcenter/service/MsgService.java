package com.yk.mqcenter.service;

import java.util.List;

import com.yk.mqcenter.dto.Msg;
import com.yk.mqcenter.exception.MqServiceException;

/**
 * @author yzw
 * @date 2019/12/19 16:14
 */
public interface MsgService {

    /**
     * 发送消息
     *
     * @param msg
     * @return msgId    消息id
     */
    String sendMsg(Msg msg) throws MqServiceException;

    /**
     * 取消消息
     *
     * @param msgIds 消息id列表
     * @return
     */
    boolean cancelMsg(List<String> msgIds) throws MqServiceException;

}
