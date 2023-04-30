package com.example.client.netty;


import com.example.client.RpcClient;
import com.example.client.RpcClientProxy;
import com.example.client.loadbalancer.RandomLoadBalancer;
import com.example.common.api.HelloService;
import com.example.common.serializer.CommonSerializer;
import com.example.common.util.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.JSON_SERIALIZER,new RandomLoadBalancer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("netty-client");
        for(int i=0;i<10;i++){
            int j = i;
            threadPool.execute(()->{
                String res = helloService.hello(j, "This is a message");
                System.out.println(j+res);
            });
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("线程执行中断");
        }
        ChannelProvider.closeChannel();
    }

}


