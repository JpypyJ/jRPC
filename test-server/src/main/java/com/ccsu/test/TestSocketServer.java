package com.ccsu.test;

import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.serializer.HessianSerializer;
import com.ccsu.rpc.transport.socket.server.SocketServer;

/**
 * 测试服务端
 *
 * @author J
 */

public class TestSocketServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9999);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }
}
