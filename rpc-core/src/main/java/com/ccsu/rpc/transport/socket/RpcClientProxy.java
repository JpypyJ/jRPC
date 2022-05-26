package com.ccsu.rpc.transport.socket;

import com.ccsu.rpc.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RPC 客户端动态代理
 *
 * @author J
 */

public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    /**
     * 需要调用的服务端的ip
     */
    private String host;

    /**
     * 需要调用的服务端的端口
     */
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 得到 clazz 的代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this);
    }

    /**
     * 当客户端调用远程方法时，代理对象传输网络请求
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        // 生成一个 RpcRequest 对象发送到服务端
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        // 创建Rpc客户端，调用客户端的发送请求的方法
        RpcClient rpcClient = new RpcClient();
        // 然返回服务端的响应数据
        return rpcClient.sendRequest(rpcRequest, host, port);
    }
}
