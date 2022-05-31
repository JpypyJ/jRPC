package com.ccsu.rpc.transport;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.serializer.CommonSerializer;

/**
 * 客户端的通用接口
 *
 * @author J
 */

public interface RpcClient {

    /**
     * 发送 RpcRequest 消息
     */
    Object sendRequest(RpcRequest rpcRequest);

    /**
     * 初始化序列化器
     */
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
}
