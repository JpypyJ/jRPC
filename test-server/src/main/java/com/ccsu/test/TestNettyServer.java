package com.ccsu.test;

import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.netty.server.NettyServer;

/**
 * 测试Netty服务端
 *
 * @author J
 */

public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.HESSIAN_SERIALIZER);
        server.publishService(helloService, HelloService.class);
    }
}
