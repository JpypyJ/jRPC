package com.ccsu.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化算法
 *
 * @author J
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO(0),
    JSON(1);
    private final int code;
}
