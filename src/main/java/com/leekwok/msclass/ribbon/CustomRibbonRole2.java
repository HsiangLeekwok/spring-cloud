package com.leekwok.msclass.ribbon;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.HealthService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.ConsulServer;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/21 15:29<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 自定义 ribbon 负载规则<br/>
 * <b>Description</b>:
 */
public class CustomRibbonRole2 extends AbstractLoadBalancerRule {

    private static final Logger logger = LoggerFactory.getLogger(CustomRibbonRole2.class);

    @Autowired
    ConsulDiscoveryProperties consulDiscoveryProperties;

    @Autowired
    ConsulClient consulClient;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        // 1. 获取微服务列表
        ILoadBalancer loadBalancer = getLoadBalancer();
        ZoneAwareLoadBalancer zoneAwareLoadBalancer = (ZoneAwareLoadBalancer) loadBalancer;
        // 想要调用的微服务的名称
        String name = zoneAwareLoadBalancer.getName();
        List<String> tags = consulDiscoveryProperties.getTags();
        String jifang = tags
                .stream()
                .filter(tag -> tag.startsWith("JIFANG"))
                .findFirst()
                .orElse(null);

        // 2. 筛选出相同机房的实例
        // 2.1 根据名称找到所有健康的实例列表
        Response<List<HealthService>> responseServices = consulClient.getHealthServices(name, null, true, QueryParams.DEFAULT);
        // 当前健康的微服务列表
        List<HealthService> healthServices = responseServices.getValue();
        logger.info("health services count: " + healthServices.size());
        // 2.2 筛选相同机房的实例
        List<HealthService> jiFangMatchedServices = healthServices
                .stream()
                .filter(healthService -> healthService.getService().getTags().contains(jifang))
                .collect(Collectors.toList());
        List<ConsulServer> servers;
        if (CollectionUtils.isEmpty(jiFangMatchedServices)) {
            servers = healthServices.stream().map(ConsulServer::new).collect(Collectors.toList());
        } else {
            servers = jiFangMatchedServices.stream().map(ConsulServer::new).collect(Collectors.toList());
        }

        // 3. 随机返回1个实例
        return randomChoose(servers);
    }

    public Server randomChoose(List<ConsulServer> servers) {
        int i = ThreadLocalRandom.current().nextInt(servers.size());
        return servers.get(i);
    }
}
