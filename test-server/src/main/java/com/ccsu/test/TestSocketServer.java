package com.ccsu.test;

import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.socket.server.SocketServer;

/**
 * 测试服务端
 *
 * @author J
 */

public class TestSocketServer {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer("127.0.0.1", 9999, CommonSerializer.HESSIAN_SERIALIZER);
        socketServer.start();
    }
}
