package com.ccsu.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化算法
 *
 *
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    JSON(1);
    private final int code;
}
