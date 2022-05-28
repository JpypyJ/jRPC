package com.ccsu.rpc.transport;

import com.ccsu.rpc.entity.RpcRequest;

/**
 * 客户端的通用接口
 *
 * @author J
 */

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
