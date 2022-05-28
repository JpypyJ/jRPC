package com.ccsu.rpc.transport.netty;

/**
 * 服务端的通用接口
 *
 * @author J
 */
public interface RpcServer {

    /**
     * 启动服务
     */
    void start(int port);
}
