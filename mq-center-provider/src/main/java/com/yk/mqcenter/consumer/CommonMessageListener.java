package com.yk.mqcenter.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.yk.mqcenter.consts.Consts;
import com.yk.mqcenter.core.InvokerCallback;
import com.yk.mqcenter.enums.CallbackType;
import com.yk.mqcenter.util.GenericServiceUtil;
import com.yk.mqcenter.util.RedisUtil;
import com.yk.mqcenter.util.RestUtil;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CommonMessageListener implements MessageListener {

    private RedisUtil redisUtil;

    CommonMessageListener(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 消费消息接口，由应用来实现<br>
     * 网络抖动等不稳定的情形可能会带来消息重复，对重复消息敏感的业务可对消息做幂等处理
     *
     * @param message 消息
     * @param context 消费上下文
     * @return 消费结果，如果应用抛出异常或者返回Null等价于返回Action.ReconsumeLater
     * @see <a href="https://help.aliyun.com/document_detail/44397.html">如何做到消费幂等</a>
     */
    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("==>receive msg={}, context={}", message, context);

        try {
            Properties userProperties = message.getUserProperties();
            String callbackKey = userProperties.getProperty(Consts.MQ_CALLBACK_KEY);
            String msgId = userProperties.getProperty(Consts.MQ_MSG_ID);
            String tag = message.getTag();
            log.info("callbackKey={}, msgId={}, tag={}", callbackKey, msgId, tag);
            if (StringUtils.isAnyBlank(callbackKey, msgId)) {
                log.error("数据错误");
                // 抛弃数据
                return Action.CommitMessage;
            }

            return handleMsg(callbackKey, msgId, tag, new String(message.getBody()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Action.ReconsumeLater;
        }
    }

    /**
     * 处理数据
     *
     * @param callbackKey
     * @param msgId
     * @param tag
     * @param body
     * @return
     */
    private Action handleMsg(String callbackKey, String msgId, String tag, String body) throws Exception {
        // 判断msgId是否在取消列表中
        if (redisUtil.sismember(Consts.REDIS_CANCELED, msgId)) {
            log.info("msgId已取消发送");
            redisUtil.sRemove(Consts.REDIS_CANCELED, msgId);
            return Action.CommitMessage;
        }

        // redis查询callback
        InvokerCallback invokerCallback = (InvokerCallback) redisUtil.get(Consts.REDIS_CALLBACK + callbackKey);
        if (invokerCallback == null) {
            log.error("callback未查询到");
            return Action.ReconsumeLater;
        }

        // 比较tag
        tag = tag == null ? "" : tag;
        String cbTag = invokerCallback.getTag() == null ? "" : invokerCallback.getTag();
        if (!tag.equalsIgnoreCase(cbTag)) {
            log.error("msg和callback tag不一致");
            return Action.CommitMessage;
        }

        callback(invokerCallback, msgId, body);

        log.info("消息回调完成. msgId={}", msgId);

        return Action.CommitMessage;
    }

    /**
     * 回调
     *
     * @param callback
     * @param body
     */
    private void callback(InvokerCallback callback, String msgId, String body) {
        if (CallbackType.DUBBO.equals(callback.getCallbackType())) {
            log.info("开始dubbo回调");
            // dubbo调用
            GenericService genericService = GenericServiceUtil.get(callback.getGroup(), callback.getInterfaceClass());
            String[] invokeParamTypes = new String[]{"java.lang.String", "java.lang.String"};
            Object[] invokeParams = new Object[]{msgId, body};
            genericService.$invoke(callback.getMethod(), invokeParamTypes, invokeParams);
        } else {
            log.info("开始http回调");
            // http调用
            Map<String, String> map = new HashMap<>(2);
            map.put("msgId", msgId);
            map.put("body", body);
            RestUtil.doPostByForm(callback.getCallbackUrl(), map);
        }
    }
}
