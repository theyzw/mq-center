package com.yk.mqcenter.dto;

import java.io.Serializable;

import com.yk.mqcenter.enums.CallbackType;
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
public class Callback implements Serializable {

    private static final long serialVersionUID = 7110289968648182587L;

    /**
     * 回调类型
     */
    private CallbackType callbackType;

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

}
