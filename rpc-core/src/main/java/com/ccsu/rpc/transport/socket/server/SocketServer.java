package com.ccsu.rpc.transport.socket.server;

import com.ccsu.rpc.registry.ServiceRegistry;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.RequestHandler;
import com.ccsu.rpc.transport.socket.RequestHandlerThread;
import com.ccsu.rpc.transport.RpcServer;
import com.ccsu.rpc.util.ThreadPoolFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket方式远程方法调用的服务端
 */
public class SocketServer implements RpcServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private final ServiceRegistry serviceRegistry;
    private RequestHandler requestHandler = new RequestHandler();
    private CommonSerializer serializer;

    // 初始化线程池
    public SocketServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
    }

    /**
     * 启动服务
     */
    @Override
    public void start(int port) {

        // 创建 ServerSocket 对象，并绑定一个端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动！");
            Socket socket;

            // 通过 accept 监听客户端的请求
            while((socket = serverSocket.accept()) != null) {
                logger.info("客户端建立连接！ip为：{}", socket.getInetAddress());

                // 调用线程池的线程处理客户端的请求
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry, serializer));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
