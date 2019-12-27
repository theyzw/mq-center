package com.yk.test.mqcenter.runner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yk.mqcenter.dto.Callback;
import com.yk.mqcenter.dto.Msg;
import com.yk.mqcenter.enums.CallbackType;
import com.yk.mqcenter.enums.MsgType;
import com.yk.mqcenter.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author yzw
 * @date 2019/12/25 17:53
 */
@Slf4j
@Component
@Order(value = 1)
public class TestRunner implements ApplicationRunner {

    @Reference
    private MsgService msgService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("==>start send");

        String group = "rdc";
        String interfaceClass = "com.yk.test.mqcenter.CallbackService";
        String method = "callback";

        Callback callback = Callback.builder().callbackType(CallbackType.DUBBO).group(group)
                .interfaceClass(interfaceClass).method(method).build();
        for (int i = 0; i < 30; i++) {
            Msg msg = Msg.builder().appId(110).msgType(MsgType.DELAYED).body("测试数据" + i)
                    .deliverTime(System.currentTimeMillis() + (i + 5) * 1000).callback(callback).build();
            String msgId = msgService.sendMsg(msg);
            System.out.println("msgId=" + msgId);
        }

        log.info("==>end send");
    }
}
