package com.yk.mqcenter.util;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yzw
 * @date 2019/12/19 20:26
 */
@Slf4j
public class GenericServiceUtil {

    private static final String APP_NAME = "rdc-callback";
    private static final String PROTOCOL = "zookeeper";
    private static final String REGISTRY_ADDRESS = "registryAddress";

    /**
     * @param group
     * @param interfaceClass
     * @return
     */
    public static GenericService get(String group, String interfaceClass) {
        String registryAddress = ApplicationContextUtil.getPropertyByKey(REGISTRY_ADDRESS);
        if (StringUtils.isBlank(registryAddress)) {
            log.error("dubbo registry address is null");
            return null;
        }

        ApplicationConfig applicationConfig = new ApplicationConfig(APP_NAME);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(registryAddress);
        registryConfig.setCheck(false);
        registryConfig.setGroup(group);
        //registryConfig.setVersion("1.0.0");
        registryConfig.setProtocol(PROTOCOL);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        // 接口名
        reference.setInterface(interfaceClass);
        // 声明为泛化接口
        reference.setGeneric(true);

        // dubbo自带缓存
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();

        // ReferenceConfig实例是个很重的实例，每个ReferenceConfig实例里面都维护了与服务注册中心的一个长链，并且维护了与所有服务提供者的的长链。
        // cache.get方法中会缓存 reference对象，并且调用reference.get方法启动ReferenceConfig，并返回经过代理后的服务接口的对象
        // ReferenceConfig get()方法会调用init()方法。
        // get到null时putIfAbsent
        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用
        return cache.get(reference);
    }
}
