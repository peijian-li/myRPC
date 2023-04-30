package com.example.client;

import com.example.common.entity.RpcRequest;
import com.example.common.serializer.CommonSerializer;


public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.JSON_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);

}
