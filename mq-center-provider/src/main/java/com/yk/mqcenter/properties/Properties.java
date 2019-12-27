package com.yk.mqcenter.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class Properties {

    @Value("${dubbo.registry.address}")
    private String registryAddress;
}
