package com.ccsu.rpc.exception;

/**
 * 序列化异常
 *
 * @author J
 */

public class SerializeException extends RuntimeException{
    public SerializeException(String msg) {
        super(msg);
    }
}
