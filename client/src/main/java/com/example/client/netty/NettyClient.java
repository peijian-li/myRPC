package com.example.client.netty;

import com.example.client.RpcClient;
import com.example.client.discovery.NacosServiceDiscovery;
import com.example.client.discovery.ServiceDiscovery;
import com.example.client.loadbalancer.LoadBalancer;
import com.example.common.codec.CommonDecoder;
import com.example.common.codec.CommonEncoder;
import com.example.common.entity.RpcRequest;
import com.example.common.entity.RpcResponse;
import com.example.common.enumeration.RpcError;
import com.example.common.exception.RpcException;
import com.example.common.serializer.CommonSerializer;
import com.example.common.util.NacosUtil;
import com.example.common.util.RpcMessageChecker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;


public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;
    private static Map<String,Thread> threadMap=new ConcurrentHashMap<>();
    private Bootstrap bootstrap;

    public NettyClient(Integer code, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer=CommonSerializer.getByCode(code);
        bootstrap=new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new CommonEncoder(serializer))
                                .addLast(new CommonDecoder())
                                .addLast(new NettyClientHandler());
                    }});
    }

    public static Thread getThread(String requestId){
        return threadMap.get(requestId);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        RpcResponse rpcResponse;
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        Channel channel=ChannelProvider.getChannel(bootstrap,inetSocketAddress);
        channel.writeAndFlush(rpcRequest);
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
        threadMap.put(rpcRequest.getRequestId(),Thread.currentThread());
        LockSupport.parkNanos(5*1000000000L);
        rpcResponse = channel.attr(key).get();
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }

}
