package com.yk.mqcenter.config;

import javax.annotation.Resource;

import com.yk.mqcenter.properties.Properties;
import com.yk.mqcenter.util.ApplicationContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableConfigurationProperties(Properties.class)
@ImportResource(locations = {"classpath:META-INF/spring/application.xml"})
public class AppConfig {

    @Resource
    private Properties properties;

    @Bean
    @ConditionalOnMissingBean(ApplicationContextUtil.class)
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil(properties);
    }

}
