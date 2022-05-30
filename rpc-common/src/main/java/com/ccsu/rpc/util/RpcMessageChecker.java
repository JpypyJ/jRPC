package com.ccsu.rpc.util;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.enums.ResponseCode;
import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查响应号和请求号是否匹配
 *
 * @author J
 */

public class RpcMessageChecker {

    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if(rpcResponse == null) {
            logger.error("RpcMessageChecker 响应消息为空, service：{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service: " + rpcRequest.getInterfaceName());
        }
        if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
            logger.error("RpcMessageChecker 响应状态码为空或服务调用失败, service：{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service: " + rpcRequest.getInterfaceName());
        }
        if(!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcError.REQUEST_NOT_MATCH);
        }
    }
}
