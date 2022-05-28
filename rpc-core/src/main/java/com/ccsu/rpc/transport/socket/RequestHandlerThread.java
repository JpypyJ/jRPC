package com.ccsu.rpc.transport.socket;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.registry.ServiceRegistry;
import com.ccsu.rpc.transport.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 处理 RpcRequest 的工作线程
 */
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            //接收RpcRequest对象，解析并调用
            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();

            // 通过反射拿到 RpcRequest 对象中的接口
            String interfaceName = rpcRequest.getInterfaceName();

            // 得到客户端请求接口的实现类
            Object service = serviceRegistry.getService(interfaceName);

            // 处理器执行客户端所需要调用的方法
            Object result = requestHandler.handler(rpcRequest, service);

            // 将结果返回给客户端
            outputStream.writeObject(RpcResponse.success(result));
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用处理器时发生错误", e);
        }
    }
}
