package com.ccsu.test;

import com.ccsu.rpc.api.HelloMessage;
import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.RpcClient;
import com.ccsu.rpc.transport.RpcClientProxy;
import com.ccsu.rpc.transport.netty.client.NettyClient;

import java.io.IOException;

/**
 * 测试Netty客户端
 *
 * @author J
 */

public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.HESSIAN_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloMessage message = new HelloMessage(1, "HY, I LOVE YOU!");
        String result = helloService.sayHello(message);
        System.out.println(result);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
