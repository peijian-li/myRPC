package com.example.server.provider;

import com.example.common.enumeration.RpcError;
import com.example.common.exception.RpcException;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private static final Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        serviceMap.put(serviceName, service);
        log.info("向接口: {} 注册服务: {}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }

}


