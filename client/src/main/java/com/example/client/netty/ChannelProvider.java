package com.example.client.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ChannelProvider {

    private static final Map<String, Channel> channels = new HashMap<>();

    public static Channel getChannel(Bootstrap bootstrap,InetSocketAddress inetSocketAddress) {
        String key=inetSocketAddress.toString();
        Channel channel=channels.get(key);
        if(channel!=null&&channel.isActive())
            return channel;
        synchronized (ChannelProvider.class){
            channel=channels.get(key);
            if(channel!=null&&channel.isActive())
                return channel;
            try {
                channel=bootstrap.connect(inetSocketAddress).sync().channel();
            } catch (InterruptedException e) {
                log.error("连接客户端时有错误发生", e);
                channels.remove(key);
                return null;
            }
            channels.put(key,channel);
            return channel;
        }
    }

    public static void closeChannel(){
        for(Map.Entry<String, Channel> entry:channels.entrySet()){
            Channel channel=entry.getValue();
            channel.close();
        }
        System.exit(0);
    }

}
