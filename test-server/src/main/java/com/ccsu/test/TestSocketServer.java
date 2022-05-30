package com.ccsu.test;

import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.registry.DefaultServiceRegistry;
import com.ccsu.rpc.serializer.KryoSerializer;
import com.ccsu.rpc.transport.socket.server.SocketServer;

/**
 * 测试服务端
 *
 * @author J
 */

public class TestSocketServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.start(9999);
    }
}
