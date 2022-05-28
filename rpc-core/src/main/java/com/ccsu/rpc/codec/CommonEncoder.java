package com.ccsu.rpc.codec;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.enums.PackageType;
import com.ccsu.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 通用的编码器
 *
 * @author J
 */

public class CommonEncoder extends MessageToByteEncoder {
    // 魔数
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    // 序列化器
    private CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        // 写入魔数
        out.writeInt(MAGIC_NUMBER);
        // 写入调用类型，请求 or 响应
        if(msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 写入序列化算法类型
        out.writeInt(serializer.getCode());
        // 序列化
        byte[] bytes = serializer.serializer(msg);
        // 写入数据长度
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
