package com.yk.mqcenter.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型
 *
 * @author yzw
 * @date 2019/07/18 11:45
 */
public enum MsgType {

    /** 定时/延时消息 */
    DELAYED(1, "定时/延时消息", "delayed", "GID_industry"),
    ///** 普通消息 */
    //COMMON(2, "普通消息", "", ""),
    ///** 全局顺序消息 */
    //GLOBAL_SORTED(3, "全局顺序消息", "", ""),
    ///** 分区顺序消息 */
    //PARTITION_SORTED(4, "分区顺序消息", "", ""),
    ///** 事务消息 */
    //TRANSACTION(5, "事务消息", "", ""),

    ;

    private static Map<Integer, MsgType> VALUE_MAP = new HashMap<Integer, MsgType>();

    static {
        for (MsgType p : MsgType.values()) {
            VALUE_MAP.put(p.getValue(), p);
        }
        VALUE_MAP = Collections.unmodifiableMap(VALUE_MAP);
    }

    private int value;
    private String name;
    public String topic;
    private String groupId;

    MsgType(int value, String name, String topic, String groupId) {
        this.value = value;
        this.name = name;
        this.topic = topic;
        this.groupId = groupId;
    }

    public static MsgType get(Integer value) {
        return VALUE_MAP.get(value);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getTopic() {
        return topic;
    }

    public String getGroupId() {
        return groupId;
    }
}