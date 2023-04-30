package com.example.server.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.example.common.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


public class NacosServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:", e);
        }
    }

}
