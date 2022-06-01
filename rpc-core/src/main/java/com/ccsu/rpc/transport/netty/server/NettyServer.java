package com.ccsu.rpc.transport.netty.server;

import com.ccsu.rpc.codec.CommonDecoder;
import com.ccsu.rpc.codec.CommonEncoder;
import com.ccsu.rpc.hook.ShutdownHook;
import com.ccsu.rpc.provider.ServiceProviderImpl;
import com.ccsu.rpc.registry.NacosServiceRegistry;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * NIO 的服务端
 *
 * @author J
 */

public class NettyServer extends AbstractRpcServer {

    private CommonSerializer serializer;


    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getSerializerByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHook();
        // 用于处理客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理后续客户端的IO事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 初始化Netty服务端启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 全连接的最大连接数，即TCP三次握手成功
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 开启TCP底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    // 不启用 Nagle 算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 绑定端口，启动Netty服务端，阻塞等待结果
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("NettyServer 启动服务器时发生错误：", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
