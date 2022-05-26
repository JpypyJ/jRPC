package com.ccsu.rpc.transport.socket;

import com.ccsu.rpc.entity.RpcRequest;
import com.ccsu.rpc.entity.RpcResponse;
import com.ccsu.rpc.enums.ResponseCode;
import com.ccsu.rpc.enums.RpcError;
import com.ccsu.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 客户类通用接口
 */

public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        // 创建 Socket 对象，并绑定服务端的ip和端口
        try (Socket socket = new Socket(host, port)) {
            // 通过输出流向服务器发送请求信息
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            // 通过输入流接收服务器的响应信息
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeObject(rpcRequest);
            outputStream.flush();
            // 返回服务端的响应消息
            RpcResponse rpcResponse = (RpcResponse) inputStream.readObject();
            if(rpcResponse == null) {
                logger.error("RpcClient响应消息为空, service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service: " + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("RpcClient响应状态码为空或服务调用失败, service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service: " + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (ClassNotFoundException | IOException e) {
            logger.error("RpcClient sendRequest读写数据错误", e);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE);
        }
    }
}