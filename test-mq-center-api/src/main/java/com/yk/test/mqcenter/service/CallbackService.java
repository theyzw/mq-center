package com.yk.test.mqcenter.service;

/**
 * @author yzw
 * @date 2019/12/26 11:03
 */
public interface CallbackService {

    void callback(String msgId, String body);
}
