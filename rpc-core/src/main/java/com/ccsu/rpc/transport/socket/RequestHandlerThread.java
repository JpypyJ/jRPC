package com.ccsu.rpc.transport.socket;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.registry.ServiceRegistry;
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
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            //接收RpcRequest对象，解析并调用
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);

            // 通过反射拿到 RpcRequest 对象中的接口
            String interfaceName = rpcRequest.getInterfaceName();

            // 处理器执行客户端所需要调用的方法
            Object result = requestHandler.handler(rpcRequest);

            // 将结果返回给客户端
            RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, rpcResponse, serializer);
        } catch (IOException e) {
            logger.error("调用处理器时发生错误", e);
        }
    }
}
