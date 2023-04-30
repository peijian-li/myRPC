package com.example.server.socket;

import com.example.common.util.ThreadPoolFactory;
import com.example.server.handler.RequestHandler;
import com.example.server.hook.ShutdownHook;
import com.example.server.provider.ServiceProviderImpl;
import com.example.server.registry.NacosServiceRegistry;
import com.example.server.AbstractRpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SocketServer extends AbstractRpcServer {

    private final ExecutorService threadPool=ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        scanServices();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务器启动……");
            ShutdownHook.addClearAllHook(host,port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }

}
