package com.ccsu.rpc.transport.socket.server;

import com.ccsu.rpc.factory.ThreadPoolFactory;
import com.ccsu.rpc.handler.RequestHandler;
import com.ccsu.rpc.hook.ShutdownHook;
import com.ccsu.rpc.provider.ServiceProviderImpl;
import com.ccsu.rpc.registry.NacosServiceRegistry;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.AbstractRpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Socket方式远程方法调用的服务端
 *
 * @author J
 */
public class SocketServer extends AbstractRpcServer {
    private final ExecutorService threadPool;

    private RequestHandler requestHandler = new RequestHandler();
    private CommonSerializer serializer;


    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }
    // 初始化线程池
    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getSerializerByCode(serializer);
        scanServices();
    }

    /**
     * 启动服务
     */
    @Override
    public void start() {

        // 创建 ServerSocket 对象，并绑定一个端口
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器正在启动！");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;

            // 通过 accept 监听客户端的请求
            while((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接！ip为：{}，port为：{}", socket.getInetAddress(), socket.getPort());

                // 调用线程池的线程处理客户端的请求
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("SocketServer 连接时有错误发生", e);
        }
    }

}
