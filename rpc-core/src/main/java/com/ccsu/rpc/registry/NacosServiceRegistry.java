package com.ccsu.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import com.ccsu.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Nacos服务注册中心
 *
 * @author J
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("NacosServiceRegistry 注册服务时有错误发生：", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILURE);
        }
    }

}
