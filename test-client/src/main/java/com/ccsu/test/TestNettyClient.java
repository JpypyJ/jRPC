package com.ccsu.test;

import com.ccsu.rpc.api.HelloMessage;
import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.serializer.HessianSerializer;
import com.ccsu.rpc.transport.RpcClient;
import com.ccsu.rpc.transport.RpcClientProxy;
import com.ccsu.rpc.transport.netty.client.NettyClient;

/**
 * 测试Netty客户端
 *
 * @author J
 */

public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new HessianSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloMessage message = new HelloMessage(1, "HY, I LOVE YOU!");
        String result = helloService.sayHello(message);
        System.out.println(result);
    }
}
