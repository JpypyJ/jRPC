package com.ccsu.rpc.registry;

import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServerRegistry{

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

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
    public synchronized <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        // 如果该服务已注册，则直接返回
        if(registeredService.contains((serviceName))){
            return;
        }
        registeredService.add(serviceName);
        // 得到该服务所有实现的接口
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务：{}", interfaces, serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
