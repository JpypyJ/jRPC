package com.ccsu.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现通用接口
 *
 * @author J
 */

public interface ServiceDiscovery {
    /**
     * 根据服务名称查找服务提供者的地址
     *
     * @param serviceName  服务名称
     * @return  服务提供者的地址
     */
    InetSocketAddress lookupService(String serviceName);
}
