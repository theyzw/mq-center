package com.yk.mqcenter.util;

import com.yk.mqcenter.consts.Consts;

/**
 * @author yzw
 * @date 2019/12/25 14:25
 */
public class MsgIdUtil {

    public static String getId(Integer appid) {
        RedisUtil redisUtil = ApplicationContextUtil.getBean(RedisUtil.class);

        String key = Consts.REDIS_MSG_ID + appid;
        long value = redisUtil.incr(key);
        return appid + "-" + value;
    }
}
