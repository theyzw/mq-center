package com.yk.mqcenter.util;

import com.yk.mqcenter.core.InvokerCallback;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author yzw
 * @date 2019/12/24 22:38
 */
public class CallbackUtil {

    public static String getCallbackKey(InvokerCallback invokerCallback) {
        String callback = JSONObject.toJSONString(invokerCallback);
        return DigestUtils.md5Hex(callback).toLowerCase();
    }

}
