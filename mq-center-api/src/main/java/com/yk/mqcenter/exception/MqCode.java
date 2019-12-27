package com.yk.mqcenter.exception;

import java.io.Serializable;

/**
 *
 */
public enum MqCode implements Serializable {

    MSG_ID_INVALID(1, "消息id无效"),
    MSG_TYPE_INVALID(2, "消息类型无效"),
    MSG_BODY_EMPTY(3, "消息体不能为空"),
    DELIVER_TIME_INVALID(4, "发送时间无效"),
    DELIVER_TIME_NOT_SUPPORT(5, "发送时间不支持"),
    CALLBACK_INVALID(6, "回调不能为空"),
    CALLBACK_TYPE_INVALID(7, "回调类型无效"),
    DUBBO_INVALID(8, "dubbo回调参数错误"),
    REST_INVALID(9, "rest回调参数错误"),

    ;

    private Integer code;
    private String message;

    MqCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return this.message;
    }

}