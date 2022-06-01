package com.ccsu.rpc.provider;

import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册表，保存服务端本地服务
 *
 *
 */

public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    /**
     * k：服务名称(实现的接口) 如 key：HelloService 的完整类名
     * v：接口的实现类  如 HelloServiceImpl
     *
     * 缺点：一个接口只能有一个实现类
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 存储已经注册的接口实现类
     * 如 HelloServiceImpl
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        // 如果该服务已注册，则直接返回
        if(registeredService.contains((serviceName))){
            return;
        }
        registeredService.add(serviceName);

        serviceMap.put(serviceName, service);
        logger.info("ServiceProviderImpl 向接口：{} 注册服务：{}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
