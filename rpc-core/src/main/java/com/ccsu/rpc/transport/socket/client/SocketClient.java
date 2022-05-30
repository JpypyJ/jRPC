package com.ccsu.rpc.transport.socket.client;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.enums.ResponseCode;
import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import com.ccsu.rpc.serializer.CommonSerializer;
import com.ccsu.rpc.transport.RpcClient;
import com.ccsu.rpc.transport.socket.util.ObjectReader;
import com.ccsu.rpc.transport.socket.util.ObjectWriter;
import com.ccsu.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Socket方式远程方法调用的客户端
 *
 * @author J
 */

public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final String host;
    private final int port;
    private CommonSerializer serializer;

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("SocketClient 未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 创建 Socket 对象，并绑定服务端的ip和端口
        try (Socket socket = new Socket(host, port)) {
            // 通过输出流向服务器发送请求信息
            OutputStream outputStream = socket.getOutputStream();
            // 通过输入流接收服务器的响应信息
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            // 返回服务端的响应消息
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("RpcClient sendRequest读写数据错误", e);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE);
        }
    }
}
