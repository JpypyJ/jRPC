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
    private final RpcClient rpcClient;

    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
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
    public Object invoke(Object proxy, Method method, Object[] args) {
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        // 生成一个 RpcRequest 对象发送到服务端
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
                                                method.getName(),
                                                args,
                                                method.getParameterTypes());
        return rpcClient.sendRequest(rpcRequest);
    }
}
