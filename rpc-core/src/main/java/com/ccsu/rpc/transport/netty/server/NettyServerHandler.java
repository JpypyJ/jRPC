package com.ccsu.rpc.transport.netty.server;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.factory.SingletonFactory;
import com.ccsu.rpc.handler.RequestHandler;
import com.ccsu.rpc.factory.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;


/**
 * Netty中处理RpcRequest的Handler
 *
 * @author J
 */

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final RequestHandler requestHandler;
    private final ExecutorService threadPool;

    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        threadPool.execute(() -> {
            try {
                logger.info("服务器收到请求：{}", msg);
                Object result = requestHandler.handler(msg);
                logger.info("服务端发送响应：{}", result);
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
                future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("NettyServerHandler 处理远程调用时有错误发生：", cause.getMessage());
        ctx.close();
    }
}
