package com.ccsu.rpc.serializer;

/**
 * 通用的序列化接口
 *
 * @author J
 */

public interface CommonSerializer {
    /**
     * 序列化
     */
    byte[] serializer(Object obj);

    /**
     * 反序列化
     */
    Object deserializer(byte[] bytes, Class<?> clazz);

    /**
     * 得到序列化器的序号
     */
    int getCode();

    /**
     * 通过类型序号得到指定的序列化器
     */
    static CommonSerializer getSerializerByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            default:
                return null;
        }
    }
}
