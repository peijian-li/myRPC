package com.example.server.service;


import com.example.common.api.HelloService;
import com.example.server.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(int id,String message) {
        log.info("接收到：{}", message);
        return "hello," + id;
    }

}
