package com.ccsu.rpc.transport.socket.util;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.enums.PackageType;
import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import com.ccsu.rpc.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Socket方式从输入流读取字节并反序列化
 *
 * @author J
 */

public class ObjectReader {

    private static final Logger logger = LoggerFactory.getLogger(ObjectReader.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static Object readObject(InputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes);
        int magic = bytesToInt(bytes);
        if(magic != MAGIC_NUMBER) {
            logger.error("不识别的协议包：{}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        in.read(bytes);
        int packageCode = bytesToInt(bytes);
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if(packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包：{}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        in.read(bytes);
        int serializerCode = bytesToInt(bytes);
        CommonSerializer serializer = CommonSerializer.getSerializerByCode(serializerCode);
        if(serializer == null) {
            logger.error("不识别的反序列化器：{}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        in.read(bytes);
        int length = bytesToInt(bytes);
        byte[] dataBytes = new byte[length];
        in.read(dataBytes);
        return serializer.deserializer(dataBytes, packageClass);
    }

    public static int bytesToInt(byte[] src) {
        int value;
        value = (src[0] & 0xFF)
                | ((src[1] &  0xFF) << 8)
                | ((src[2] &  0xFF) << 16)
                | ((src[3] &  0xFF) << 24);
        return value;
    }

}
