package com.example.client.discovery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.example.client.loadbalancer.LoadBalancer;
import com.example.common.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private Map<String,List<Instance>> instanceMap=new HashMap<>();
    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        new Thread(()->{
            try {
                instanceMap.put("HelloService",NacosUtil.getAllInstance("HelloService"));
                instanceMap.put("ByeService",NacosUtil.getAllInstance("ByeService"));
            } catch (NacosException e) {
                log.error("缓存服务时有错误发生:", e);
            }finally {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        instanceMap.put("HelloService", NacosUtil.getAllInstance("HelloService"));
                        instanceMap.put("ByeService", NacosUtil.getAllInstance("ByeService"));
                    } catch (InterruptedException e) {
                        log.error("等待时有错误发生:", e);
                    } catch (NacosException e) {
                        log.error("缓存服务时有错误发生:", e);
                        continue;
                    }
                }
            }
        }).start();
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = instanceMap.get(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (Exception e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}

