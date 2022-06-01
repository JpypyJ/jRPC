package com.ccsu.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡器
 *
 * @author J
 */

public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
