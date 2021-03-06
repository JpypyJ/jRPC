package com.ccsu.rpc.exception;

import com.ccsu.rpc.enums.RpcError;

/**
 * RPC 调用异常
 *
 * @author J
 */

public class RpcException extends RuntimeException{

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + " : " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
