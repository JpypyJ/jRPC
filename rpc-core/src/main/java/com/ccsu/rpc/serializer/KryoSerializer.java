package com.ccsu.rpc.serializer;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.enums.SerializerCode;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 基于Kryo的序列化器
 *
 * @author J
 */

public class KryoSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /**
     * 由于 Kryo 不是线程安全的，每个线程应该有自己的 Kryo、Input 和 Output
     * 解决：
     * 1、使用 ThreadLocal 存储 Kryo 对象
     * 2、使用Kryo自带的对象池
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 循环引用检测
        kryo.setReferences(true);
        return kryo;
    });

    @Override
    public byte[] serializer(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            // 创建一个Kryo对象并存储到ThreadLocal
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // 将对象写入Output中
            kryo.writeObject(output, obj);
            // 从ThreadLocal移除Kryo
            KRYO_THREAD_LOCAL.remove();
            return output.toBytes();
        } catch (IOException e) {
            logger.error("Kryo 序列化时出现问题", e);
            return null;
        }
    }

    @Override
    public Object deserializer(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            Object obj = kryo.readObject(input, clazz);
            KRYO_THREAD_LOCAL.remove();
            return obj;
        } catch (IOException e) {
            logger.error("Kryo 反序列化时出现问题", e);
            return null;
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.KRYO.getCode();
    }
}
