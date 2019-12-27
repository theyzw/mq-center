package com.yk.mqcenter.core;

import java.io.Serializable;

import com.yk.mqcenter.dto.Msg;
import com.yk.mqcenter.enums.CallbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author yzw
 * @date 2019/12/18 15:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvokerCallback implements Serializable {

    private static final long serialVersionUID = 3204605598609926489L;

    /**
     * 回调类型
     */
    private CallbackType callbackType;

    /**
     * tag
     */
    private String tag;

    /**
     * dubbo - 回调group
     */
    private String group;

    /**
     * dubbo - 回调接口，包括包名
     */
    private String interfaceClass;

    /**
     * dubbo - 回调方法
     */
    private String method;

    /**
     * rest - 回调url
     */
    private String callbackUrl;

    public static InvokerCallback buildFromMsg(Msg msg) {
        InvokerCallback invokerCallback = new InvokerCallback();
        BeanUtils.copyProperties(msg.getCallback(), invokerCallback);
        invokerCallback.setTag(msg.getTag());
        return invokerCallback;
    }
}
