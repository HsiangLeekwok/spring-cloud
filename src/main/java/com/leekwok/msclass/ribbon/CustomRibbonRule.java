package com.leekwok.msclass.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.ConsulServer;
import org.springframework.cloud.consul.discovery.ConsulServerUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/21 14:56<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class CustomRibbonRule extends AbstractLoadBalancerRule {

    public static final String JIFANG = "JIFANG";
    // consul 配置属性发现类
    @Autowired
    private ConsulDiscoveryProperties consulDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // 读取配置
    }

    @Override
    public Server choose(Object key) {
        // 负载均衡算法
        // 1. 获得想要调用微服务的实例列表
        ILoadBalancer loadBalancer = this.getLoadBalancer();
        List<Server> servers = loadBalancer.getReachableServers();

        // 2. 筛选出相同机房的实例列表
        List<String> tags = consulDiscoveryProperties.getTags();// 获取当前实例配置的机房信息
        Map<String, String> metadata = ConsulServerUtils.getMetadata(tags);// 转换成 map
        List<Server> jiFangMatchedServers = servers.stream().filter(server -> {
            ConsulServer consulServer = (ConsulServer) server;
            Map<String, String> tagetMetadata = consulServer.getMetadata();
            // 查找属于同一个机房的微服务列表
            return Objects.equals(metadata.get(JIFANG), tagetMetadata.get(JIFANG));
        }).collect(Collectors.toList());
        // 3. 随机返回一个实例
        if (CollectionUtils.isEmpty(jiFangMatchedServers)) {
            return this.randomChoose(servers);
        }
        return randomChoose(jiFangMatchedServers);
    }

    public Server randomChoose(List<Server> servers) {
        int i = ThreadLocalRandom.current().nextInt(servers.size());
        return servers.get(i);
    }
}
