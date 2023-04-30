package com.example.server.socket;

import com.example.server.annotation.ServiceScan;
import com.example.server.RpcServer;

@ServiceScan
public class SocketTestServer {

    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998);
        server.start();
    }

}
