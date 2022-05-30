package com.ccsu.rpc.transport.netty.server;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.registry.DefaultServiceRegistry;
import com.ccsu.rpc.registry.ServiceRegistry;
import com.ccsu.rpc.transport.RequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Netty中处理RpcRequest的Handler
 *
 * @author J
 */

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static ServiceRegistry serviceRegistry;
    private static RequestHandler requestHandler;

    static {
        serviceRegistry = new DefaultServiceRegistry();
        requestHandler = new RequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            logger.info("NettyServerHandler 接收到请求：{}", msg);
            // 获取客户端远程调用方法的接口
            String interfaceName = msg.getInterfaceName();
            // 在服务端查询接口的实现类
            Object service = serviceRegistry.getService(interfaceName);
            // 处理RpcRequest请求
            Object result = requestHandler.handler(msg, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("NettyServerHandler处理远程调用时有错误发生：", cause.getMessage());
        ctx.close();
    }
}
