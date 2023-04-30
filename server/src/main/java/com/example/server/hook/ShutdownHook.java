package com.example.server.hook;

import com.example.common.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ShutdownHook {

    public  static void addClearAllHook(String host,int port) {
        log.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> new NacosUtil(host,port).clearRegistry()));
    }

}
