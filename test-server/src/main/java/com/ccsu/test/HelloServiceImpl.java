package com.ccsu.test;

import com.ccsu.rpc.annotation.Service;
import com.ccsu.rpc.api.HelloMessage;
import com.ccsu.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HelloService 的实现
 *
 * @author J
 */
@Service
public class HelloServiceImpl implements HelloService {

    /**
     * 使用指定类初始化日志对象，在日志输出的时候，可以打印出日志信息所在类
     */
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String sayHello(HelloMessage msg) {
        logger.info("接收到来自客户端的消息：{}", msg.getMessage());
        return "这是Impl1方法";
    }
}
