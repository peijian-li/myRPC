package com.example.client.socket;



import com.example.client.RpcClientProxy;
import com.example.client.loadbalancer.RandomLoadBalancer;
import com.example.common.api.HelloService;


public class SocketTestClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient(new RandomLoadBalancer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String res=helloService.hello(1, "This is a message");
        System.out.println(res);
    }

}
