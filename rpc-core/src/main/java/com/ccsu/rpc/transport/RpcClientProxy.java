package com.ccsu.rpc.transport;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.transport.netty.client.NettyClient;
import com.ccsu.rpc.transport.socket.client.SocketClient;
import com.ccsu.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        logger.info("RpcClientProxy 调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        // 生成一个 RpcRequest 对象发送到服务端
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),
                                                method.getDeclaringClass().getName(),
                                                method.getName(),
                                                args,
                                                method.getParameterTypes(),
                                                false);
        RpcResponse rpcResponse = null;
        if(rpcClient instanceof NettyClient) {
            CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) rpcClient.sendRequest(rpcRequest);
            try {
                rpcResponse = completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("RpcClientProxy 方法调用请求发送失败：", e);
                return null;
            }
        }
        if(rpcClient instanceof SocketClient) {
            rpcResponse = (RpcResponse) rpcClient.sendRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
