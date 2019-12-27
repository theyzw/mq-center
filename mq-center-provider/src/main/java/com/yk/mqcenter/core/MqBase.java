package com.yk.mqcenter.core;

import java.io.Serializable;

import com.yk.mqcenter.dto.Msg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author yzw
 * @date 2019/12/24 22:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MqBase implements Serializable {

    private static final long serialVersionUID = -166133269099810344L;

    /**
     * 消息id。{appId}-{自增序列}，用于取消发送消息，不允许重复
     */
    private String msgId;

    /**
     * topic
     */
    private String topic;

    /**
     * tag
     */
    private String tag;

    /**
     * 消息体
     */
    private String body;

    /**
     * 回调key
     */
    private String callbackKey;

    /**
     * 消息发送时间
     */
    private Long deliverTime;

    /**
     * 分区顺序消息中区分不同分区的关键字段，sharding key 与普通消息的 key 是完全不同的概念。
     * 全局顺序消息，该字段可以设置为任意非空字符串。
     *
     * @return String
     */
    private String shardingKey;

    public static MqBase buildFromMsg(Msg msg) {
        MqBase mqBase = new MqBase();
        BeanUtils.copyProperties(msg, mqBase);
        if (mqBase.getDeliverTime() != null && mqBase.getDeliverTime() - System.currentTimeMillis() <= 0) {
            mqBase.setDeliverTime(null);
        }
        mqBase.setTopic(msg.getMsgType().getTopic());
        return mqBase;
    }
}
