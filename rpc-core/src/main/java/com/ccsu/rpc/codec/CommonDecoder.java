package com.ccsu.rpc.codec;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.enums.PackageType;
import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import com.ccsu.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用的解码器
 *
 * @author J
 */

public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    //魔数
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) {
        // 读取数据包类型
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER) {
            logger.error("不识别的协议包：{}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        // 读取数据包类型的编码
        int packageCode = in.readInt();
        // 数据包的类型 请求/调用
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包：{}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        // 读取序列化算法的编码
        int serializerCode = in.readInt();
        // 通过序列化算法的编码得到序列化器
        CommonSerializer serializer = CommonSerializer.getSerializerByCode(serializerCode);
        if(serializer == null) {
            logger.error("不识别的序列化器：{}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        // 读取原始数据的长度
        int length = in.readInt();
        byte[] bytes = new byte[length];
        // 将原始数据读入byte数组中
        in.readBytes(bytes);
        // 反序列化成Java对象
        Object obj = serializer.deserializer(bytes, packageClass);
        list.add(obj);
    }
}
