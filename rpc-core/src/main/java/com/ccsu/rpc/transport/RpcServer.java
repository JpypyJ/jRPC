package com.ccsu.rpc.transport;

import com.ccsu.rpc.serializer.CommonSerializer;

/**
 * 服务端的通用接口
 *
 * @author J
 */
public interface RpcServer {

    /**
     * 启动服务
     */
    void start();

    /**
     * 初始化序列化器
     */
    void setSerializer(CommonSerializer serializer);

    /**
     * 注册服务
     */
    <T> void publishService(T service, Class<T> serviceClass);
}
