package com.example.server.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.example.common.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;


import java.net.InetSocketAddress;

@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务时有错误发生:", e);
        }
    }

}
