package com.ccsu.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册中心通用接口
 *
 * @author J
 */

public interface ServiceRegistry {
    /**
     * 将一个服务注册到注册表
     *
     * @param serviceName 服务名称
     * @param inetSocketAddress  服务提供者的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名称查找服务提供者的地址
     *
     * @param serviceName  服务名称
     * @return  服务提供者的地址
     */
    InetSocketAddress lookupService(String serviceName);
}
