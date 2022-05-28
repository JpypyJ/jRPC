package com.ccsu.rpc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.ccsu.rpc.enums.SerializerCode;
import com.ccsu.rpc.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 基于 Hessian 的序列化器
 *
 * @author J
 */

public class HessianSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(HessianSerializer.class);

    @Override
    public byte[] serializer(Object obj) {
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Hessian 序列化时出现错误", e);
            throw new SerializeException("Hessian 序列化时出现错误");
        } finally {
            if (hessianOutput != null) {
                try {
                    hessianOutput.close();
                } catch (IOException e) {
                    logger.error("关闭Hessian2Output时出现错误：", e);
                }
            }
        }
    }

    @Override
    public Object deserializer(byte[] bytes, Class<?> clazz) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            hessianInput = new HessianInput(byteArrayInputStream);
            return hessianInput.readObject();
        } catch (IOException e) {
            logger.error("Hessian 反序列化时出现错误", e);
            throw new SerializeException("Hessian 反序列化时出现错误");
        } finally {
            if(hessianInput != null) {
                hessianInput.close();
            }
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.HESSIAN.getCode();
    }
}
