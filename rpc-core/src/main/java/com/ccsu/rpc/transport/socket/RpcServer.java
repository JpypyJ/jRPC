package com.ccsu.rpc.transport.socket;

import com.ccsu.rpc.registry.ServerRegistry;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务端通用接口
 */
public class RpcServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ServerRegistry serverRegistry;
    private RequestHandler requestHandler = new RequestHandler();

    // 初始化线程池
    public RpcServer(ServerRegistry serverRegistry) {
        this.serverRegistry = serverRegistry;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory =
                new ThreadFactoryBuilder().setNameFormat("rpc-server-pool-%d").build();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                                            MAXIMUM_POOL_SIZE,
                                            KEEP_ALIVE_TIME,
                                            TimeUnit.SECONDS,
                                            workQueue,
                                            threadFactory);
    }

    /**
     * 启动服务
     */
    public void start(int port) {

        // 创建 ServerSocket 对象，并绑定一个端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动！");
            Socket socket;

            // 通过 accept 监听客户端的请求
            while((socket = serverSocket.accept()) != null) {
                logger.info("客户端建立连接！ip为：{}", socket.getInetAddress());

                // 调用线程池的线程处理客户端的请求
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serverRegistry));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生", e);
        }
    }
}
