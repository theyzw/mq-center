package com.yk.mqcenter.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.yk.mqcenter.BaseTest;
import com.yk.mqcenter.consts.Consts;
import com.yk.mqcenter.dto.Callback;
import com.yk.mqcenter.dto.Msg;
import com.yk.mqcenter.enums.CallbackType;
import com.yk.mqcenter.enums.MsgType;
import com.yk.mqcenter.service.MsgService;
import com.yk.mqcenter.util.RedisUtil;
import org.junit.Test;

public class MsgServiceImplTest extends BaseTest {

    @Resource
    private MsgService msgService;
    @Resource
    private RedisUtil redisUtil;

    @Test
    public void sendMsg() {
        for (int i = 0; i < 30; i++) {
            Callback callback = Callback.builder().callbackType(CallbackType.DUBBO).group("aa").interfaceClass("bb")
                    .method("callback").build();
            Msg msg = Msg.builder().appId(100101).msgType(MsgType.DELAYED).body("content")
                    .deliverTime(System.currentTimeMillis()).callback(callback).build();
            String msgId = msgService.sendMsg(msg);
            System.out.println("msgId=" + msgId);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelMsg() {
        String msgId = "110-111";
        List<String> list = Arrays.asList("110-123", msgId, "110-120", "BB");
        msgService.cancelMsg(list);

        System.out.println(redisUtil.sismember(Consts.REDIS_CANCELED, msgId));
    }

}