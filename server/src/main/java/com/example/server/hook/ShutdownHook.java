package com.example.server.hook;

import com.example.common.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    public  static void addClearAllHook(String host,int port) {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            new NacosUtil(host,port).clearRegistry();
        }));
    }

}
