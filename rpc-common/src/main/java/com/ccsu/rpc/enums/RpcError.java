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

    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到指定的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口");

    private final String message;

}
