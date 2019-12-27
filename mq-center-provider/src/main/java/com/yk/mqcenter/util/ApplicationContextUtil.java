package com.yk.mqcenter.util;

import com.yk.mqcenter.producer.DefaultSendCallback;
import com.yk.mqcenter.properties.Properties;
import com.aliyun.openservices.ons.api.SendCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yzw
 * @date 2019/12/19 21:21
 */
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    private Properties properties;

    public ApplicationContextUtil() {
    }

    public ApplicationContextUtil(Properties properties) {
        this.properties = properties;
    }

    public static String getPropertyByKey(String key) {
        Properties props = getBean(Properties.class);
        try {
            return (String) PropertyUtils.getProperty(props, key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class clz) {
        return (T) applicationContext.getBean(clz);
    }

    public static SendCallback getSendCallback(Class<? extends SendCallback> callback) {
        if (DefaultSendCallback.class.equals(callback)) {
            return new DefaultSendCallback();
        } else {
            return applicationContext.getBean(callback);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtil.applicationContext == null) {
            ApplicationContextUtil.applicationContext = applicationContext;
        }
    }
}
