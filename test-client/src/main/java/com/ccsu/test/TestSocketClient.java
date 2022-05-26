package com.ccsu.test;

import com.ccsu.rpc.api.HelloMessage;
import com.ccsu.rpc.api.HelloService;
import com.ccsu.rpc.transport.socket.RpcClientProxy;

/**
 * 测试客户端
 *
 * @author J
 */

public class TestSocketClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("localhost", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloMessage message = new HelloMessage(1, "HY, I LOVE YOU!");
        String result = helloService.sayHello(message);
        System.out.println(result);
    }
}
