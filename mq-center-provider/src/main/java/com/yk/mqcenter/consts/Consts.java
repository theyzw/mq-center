package com.yk.mqcenter.consts;

/**
 * @author yzw
 * @date 2019/07/18 19:13
 */
public class Consts {

    /**
     * redis key前缀
     */
    public final static String REDIS_KEY_PRE = "mq";

    /**
     * callback key前缀
     */
    public final static String REDIS_CALLBACK = REDIS_KEY_PRE + ":callback:";

    /**
     * cancel key前缀
     */
    public final static String REDIS_CANCELED = REDIS_KEY_PRE + ":canceled";

    /**
     * msg id key前缀
     */
    public final static String REDIS_MSG_ID = REDIS_KEY_PRE + ":msgid:";

    /**
     * 默认超时时间 30天
     */
    public final static int TIMEOUT_DEFAULT_DAY = 30;

    /**
     * message properties中的callback_key
     */
    public final static String MQ_CALLBACK_KEY = "callback_key";

    /**
     * message properties中的自定义消息id
     */
    public final static String MQ_MSG_ID = "msg_id";

}
