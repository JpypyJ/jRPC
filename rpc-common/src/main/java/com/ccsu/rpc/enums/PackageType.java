package com.ccsu.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网络传输中包的类型：请求、响应
 *
 * @author J
 */
@AllArgsConstructor
@Getter
public enum PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);
    private final int code;
}
