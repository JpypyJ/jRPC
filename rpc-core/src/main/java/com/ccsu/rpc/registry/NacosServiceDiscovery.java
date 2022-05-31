package com.ccsu.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ccsu.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Nacos服务发现中心
 *
 * @author J
 */

public class NacosServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);


    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("NacosServiceDiscovery 获取服务时有错误发生：", e);
        }
        return null;
    }
}
