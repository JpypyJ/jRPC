package com.ccsu.rpc.transport.socket.server;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.handler.RequestHandler;
import com.ccsu.rpc.transport.socket.util.ObjectReader;
import com.ccsu.rpc.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 处理 RpcRequest 的工作线程
 */
public class SocketRequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            //接收RpcRequest对象，解析并调用
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);

            // 处理器执行客户端所需要调用的方法
            Object result = requestHandler.handler(rpcRequest);

            // 将结果返回给客户端
            RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, rpcResponse, serializer);
        } catch (IOException e) {
            logger.error("SocketRequestHandlerThread 调用处理器时发生错误", e);
        }
    }
}
