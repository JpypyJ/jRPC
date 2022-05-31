package com.ccsu.test;

import com.ccsu.rpc.api.HelloMessage;
import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.serializer.HessianSerializer;
import com.ccsu.rpc.transport.RpcClient;
import com.ccsu.rpc.transport.RpcClientProxy;
import com.ccsu.rpc.transport.socket.client.SocketClient;

/**
 * 测试Socket客户端
 *
 * @author J
 */

public class TestSocketClient {
    public static void main(String[] args) {
        RpcClient client = new SocketClient(CommonSerializer.HESSIAN_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloMessage message = new HelloMessage(1, "HY, I LOVE YOU!");
        String result = helloService.sayHello(message);
        System.out.println(result);
    }
}
