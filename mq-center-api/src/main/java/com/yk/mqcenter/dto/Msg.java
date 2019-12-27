package com.yk.mqcenter.dto;

import java.io.Serializable;

import com.yk.mqcenter.enums.MsgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @date 2019/12/18 15:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Msg implements Serializable {

    private static final long serialVersionUID = -3502590640126723946L;

    /**
     * appId
     */
    private Integer appId;

    /**
     * 消息类型
     */
    private MsgType msgType;

    /**
     * tag
     */
    private String tag;

    /**
     * 消息体
     */
    private String body;

    /**
     * 回调配置
     */
    private Callback callback;

    /**
     * 定时消息 - 消息发送时间，msg，最长支持40天
     */
    private Long deliverTime;

    /**
     * 顺序消息 - 分区key
     * 分区顺序消息中区分不同分区的关键字段
     * 全局顺序消息，该字段可以设置为任意非空字符串。
     */
    private String shardingKey;

}
