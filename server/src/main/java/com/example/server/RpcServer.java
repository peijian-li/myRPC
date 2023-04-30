package com.example.server;


import com.example.common.serializer.CommonSerializer;

public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.JSON_SERIALIZER;

    void start();

    <T> void publishService(T service, String serviceName);

}

