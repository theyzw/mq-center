package com.yk.test.mqcenter.service.impl;

import com.yk.test.mqcenter.service.CallbackService;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzw
 * @date 2019/12/26 11:04
 */
@Slf4j
@Service
public class CallbackServiceImpl implements CallbackService {

    @Override
    public void callback(String msgId, String body) {
        log.info("==>receive mq msg. msgId={}, body={}", msgId, body);
    }
}
