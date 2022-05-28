package com.ccsu.rpc.serializer;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.enums.SerializerCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 使用 JSON 格式的序列化器
 *
 * @author J
 */

public class JsonSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serializer(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("JsonSerializer 序列化时发生错误：{}", e);
            return null;
        }
    }

    @Override
    public Object deserializer(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest) {
                obj = handlerRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("JsonSerializer 反序列化时发生错误：{}", e);
            return null;
        }
    }

    /**
     *
     * 因为在RpcRequest中 private Object[] parameters 将方法的参数声明为Object类型，
     *      在反序列化时，会将参数全部序列化为Object类型
     *      比如参数为 String message，int type 反序列化后全部为Object类型了，丢失了原有类型
     *
     * clazz为RpcRequest中的参数类型的类 如String message的String类，将方法参数再次反序列化为原始类型
     *      假设第i个参数类型为 String ，则clazz为String，
     *      通过objectMapper.readValue(bytes, clazz)将byte类型反序列化为String类型
     */

    private Object handlerRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for(int i = 0; i < rpcRequest.getParameters().length; i++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParamTypes()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.JSON.getCode();
    }
}
