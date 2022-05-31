package com.ccsu.rpc.provider;

/**
 * 保存和提供服务实例对象
 *
 * @author J
 */
public interface ServiceProvider {
    /**
     * 保存服务提供者
     */
    <T> void addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 获取服务提供者
     */
    Object getServiceProvider(String serviceName);

}
