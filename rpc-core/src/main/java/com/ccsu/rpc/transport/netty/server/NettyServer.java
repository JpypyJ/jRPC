package com.ccsu.rpc.transport.netty.server;

import com.ccsu.rpc.codec.CommonDecoder;
import com.ccsu.rpc.codec.CommonEncoder;
import com.ccsu.rpc.serializer.JsonSerializer;
import com.ccsu.rpc.serializer.KryoSerializer;
import com.ccsu.rpc.transport.netty.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NIO 的服务端
 *
 * @author J
 */

public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Override
    public void start(int port) {
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
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 绑定端口，启动Netty服务端，阻塞等待结果
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时发生错误：", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
