package com.ccsu.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC 调用过程产生的错误
 *
 * @author J
 */

@AllArgsConstructor
@Getter
public enum RpcError {

    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到指定的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    REQUEST_NOT_MATCH("请求号不匹配");

    private final String message;

}
