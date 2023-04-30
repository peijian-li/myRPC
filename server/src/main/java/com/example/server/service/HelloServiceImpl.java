package com.example.server.service;


import com.example.common.api.HelloService;
import com.example.server.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(int id,String message) {
        logger.info("接收到：{}", message);
        return "hello," + id;
    }

}
