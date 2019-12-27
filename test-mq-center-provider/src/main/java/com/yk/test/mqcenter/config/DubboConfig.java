package com.yk.test.mqcenter.config;

import java.util.Optional;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Value("${dubbo.protocol.name}")
    protected String dubboProtocolName;
    @Value("${dubbo.protocol.port}")
    protected Integer dubboProtocolPort;
    @Value("${dubbo.protocol.accepts}")
    protected Integer dubboProtocolAccepts;
    @Value("${dubbo.protocol.threads}")
    protected Integer dubboProtocolThreads;

    @Value("${dubbo.provider.timeout}")
    protected Integer dubboProviderTimeout;
    @Value("${dubbo.provider.delay}")
    protected Integer dubboProviderDelay;
    @Value("${dubbo.provider.retries}")
    protected Integer dubboProviderRetries;

    @Value("${dubbo.consumer.check}")
    protected Boolean dubboConsumerCheck;

    @Value("${dubbo.consumer.timeout:}")
    protected Integer dubboConsumerTimeout;
    @Value("${dubbo.consumer.retries}")
    protected Integer dubboConsumerRetries;

    @Value("${dubbo.application.name}")
    protected String dubboApplicationName;
    @Value("${dubbo.application.owner}")
    protected String dubboApplicationOwner;

    @Value("${dubbo.registry.protocol}")
    protected String dubboRegistryProtocol;
    @Value("${dubbo.registry.address}")
    protected String dubboRegistryAddress;
    @Value("${dubbo.registry.file}")
    protected String dubboRegistryFile;
    @Value("${dubbo.registry.group}")
    protected String dubboRegistryGroup;

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboApplicationName);
        applicationConfig.setOwner(dubboApplicationOwner);
        applicationConfig.setOrganization(dubboApplicationOwner);
        applicationConfig.setLogger("slf4j");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol(dubboRegistryProtocol);
        registryConfig.setAddress(dubboRegistryAddress);
        registryConfig.setFile(dubboRegistryFile);
        registryConfig.setGroup(dubboRegistryGroup);
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(dubboProtocolName);
        protocolConfig.setPort(dubboProtocolPort);
        protocolConfig.setAccepts(dubboProtocolAccepts);
        protocolConfig.setThreads(dubboProtocolThreads);
        protocolConfig.setAccesslog("true");
        return protocolConfig;
    }

    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(dubboProviderTimeout);
        providerConfig.setDelay(dubboProviderDelay);
        providerConfig.setRetries(dubboProviderRetries);
        //自定义异常filter，排除dubbo默认异常filter
        //providerConfig.setFilter("dubboExceptionFilter,-exception");
        return providerConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setCheck(dubboConsumerCheck);
        consumerConfig.setTimeout(Optional.ofNullable(dubboConsumerTimeout).orElse(null));
        consumerConfig.setRetries(dubboConsumerRetries);
        return consumerConfig;
    }
}
