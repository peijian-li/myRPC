package com.example.client.discovery;


import com.alibaba.nacos.api.naming.pojo.Instance;
import com.example.client.loadbalancer.LoadBalancer;
import com.example.common.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;


import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final Map<String,List<Instance>> instanceMap=new ConcurrentHashMap<>();
    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        new Thread(()->{
           while (true) {
               try {
                   Thread.sleep(5000);
               } catch (InterruptedException e) {
                   log.error("等待时发生错误",e);
               }
               instanceMap.clear();
           }
        }).start();
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            if(instanceMap.size()==0){
                instanceMap.put(serviceName,NacosUtil.getAllInstance(serviceName));
            }
            List<Instance> instances = instanceMap.get(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (Exception e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}

