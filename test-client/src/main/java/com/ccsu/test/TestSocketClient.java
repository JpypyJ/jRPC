package com.ccsu.test;

import com.ccsu.rpc.api.HelloMessage;
import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.serializer.KryoSerializer;
import com.ccsu.rpc.transport.RpcClientProxy;
import com.ccsu.rpc.transport.socket.client.SocketClient;

/**
 * 测试Socket客户端
 *
 * @author J
 */

public class TestSocketClient {
    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient("localhost", 9999);
        socketClient.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(socketClient);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloMessage message = new HelloMessage(1, "HY, I LOVE YOU!");
        String result = helloService.sayHello(message);
        System.out.println(result);
    }
}
