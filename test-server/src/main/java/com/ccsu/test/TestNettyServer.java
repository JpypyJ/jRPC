package com.ccsu.test;

import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.registry.DefaultServiceRegistry;
import com.ccsu.rpc.registry.ServiceRegistry;
import com.ccsu.rpc.serializer.HessianSerializer;
import com.ccsu.rpc.transport.netty.server.NettyServer;

/**
 * 测试Netty服务端
 *
 * @author J
 */

public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer nettyServer = new NettyServer();
        nettyServer.setSerializer(new HessianSerializer());
        nettyServer.start(9999);
    }
}
