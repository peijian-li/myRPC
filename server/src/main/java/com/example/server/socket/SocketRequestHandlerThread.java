package com.example.server.socket;

import com.example.common.entity.RpcRequest;
import com.example.common.entity.RpcResponse;
import com.example.server.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;


import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketRequestHandlerThread implements Runnable {

    private Socket socket;
    private RequestHandler requestHandler;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler) {
        this.socket = socket;
        this.requestHandler = requestHandler;

    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = requestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result,rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用或发送时有错误发生：", e);
        }
    }

}
