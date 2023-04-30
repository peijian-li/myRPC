package com.example.server.netty;


import com.example.server.annotation.ServiceScan;

@ServiceScan(value = "com.example.server")
public class NettyTestServer {

    public static void main(String[] args) {
        NettyServer server0 = new NettyServer("127.0.0.1", 9999);
        NettyServer server1 = new NettyServer("127.0.0.1", 9998);
        new Thread(()->{server0.start();}).start();
        new Thread(()->{server1.start();}).start();
    }

}

