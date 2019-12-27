package com.yk.mqcenter.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.yk.mqcenter.consts.Consts;
import com.yk.mqcenter.core.InvokerCallback;
import com.yk.mqcenter.core.MqBase;
import com.yk.mqcenter.dto.Callback;
import com.yk.mqcenter.dto.Msg;
import com.yk.mqcenter.enums.CallbackType;
import com.yk.mqcenter.enums.MsgType;
import com.yk.mqcenter.exception.MqCode;
import com.yk.mqcenter.exception.MqServiceException;
import com.yk.mqcenter.producer.RocketSender;
import com.yk.mqcenter.service.MsgService;
import com.yk.mqcenter.util.CallbackUtil;
import com.yk.mqcenter.util.MsgIdUtil;
import com.yk.mqcenter.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yzw
 * @date 2019/12/18 18:28
 */
@Slf4j
@Service
public class MsgServiceImpl implements MsgService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RocketSender rocketSender;

    @Override
    public String sendMsg(Msg msg) throws MqServiceException {
        log.info("发送消息. msg={}", msg);

        // 校验参数
        checkParam(msg);

        InvokerCallback invokerCallback = InvokerCallback.buildFromMsg(msg);
        String callbackKey = CallbackUtil.getCallbackKey(invokerCallback);

        MqBase mqBase = MqBase.buildFromMsg(msg);
        mqBase.setCallbackKey(callbackKey);
        String msgId = MsgIdUtil.getId(msg.getAppId());
        mqBase.setMsgId(msgId);

        redisUtil.setnx(Consts.REDIS_CALLBACK + callbackKey, invokerCallback);

        switch (msg.getMsgType()) {
            case DELAYED:
                rocketSender.sendCommonMsg(mqBase);
                break;
            default:
                break;
        }

        log.info("发送消息成功. msgId={}", msgId);

        return msgId;
    }

    @Override
    public boolean cancelMsg(List<String> msgIds) throws MqServiceException {
        log.info("取消消息. msgIds={}", msgIds);

        if (CollectionUtils.isEmpty(msgIds)) {
            throw new MqServiceException("参数错误");
        }

        //放到canceled里
        msgIds.forEach(x -> redisUtil.sadd(Consts.REDIS_CANCELED, x.trim()));

        log.info("取消消息成功");

        return true;
    }

    /**
     * 校验参数
     *
     * @param msg
     * @throws MqServiceException
     */
    private void checkParam(Msg msg) throws MqServiceException {
        // 参数校验
        if (msg == null) {
            throw new MqServiceException("参数错误");
        }

        if (msg.getAppId() == null) {
            throw new MqServiceException(MqCode.MSG_ID_INVALID);
        }

        if (msg.getMsgType() == null) {
            throw new MqServiceException(MqCode.MSG_TYPE_INVALID);
        }

        if (StringUtils.isBlank(msg.getBody())) {
            throw new MqServiceException(MqCode.MSG_BODY_EMPTY);
        }

        if (msg.getMsgType().equals(MsgType.DELAYED)) {
            if (msg.getDeliverTime() == null) {
                throw new MqServiceException(MqCode.DELIVER_TIME_INVALID);
            }
            if (DateUtil.betweenDay(new Date(), new Date(msg.getDeliverTime()), true) > 40) {
                throw new MqServiceException(MqCode.DELIVER_TIME_NOT_SUPPORT);
            }
        }

        Callback callback = msg.getCallback();

        if (callback == null) {
            throw new MqServiceException(MqCode.CALLBACK_INVALID);
        }

        if (callback.getCallbackType() == null) {
            throw new MqServiceException(MqCode.CALLBACK_TYPE_INVALID);
        }

        if (callback.getCallbackType().equals(CallbackType.DUBBO)) {
            if (StringUtils.isAnyBlank(callback.getGroup(), callback.getInterfaceClass(), callback.getMethod())) {
                throw new MqServiceException(MqCode.DUBBO_INVALID);
            }
        } else {
            if (StringUtils.isBlank(callback.getCallbackUrl())) {
                throw new MqServiceException(MqCode.REST_INVALID);
            }
        }
    }
}
