package com.example.server.netty;

import com.example.common.entity.RpcRequest;
import com.example.common.entity.RpcResponse;
import com.example.common.util.ThreadPoolFactory;
import com.example.server.handler.RequestHandler;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.ExecutorService;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private static final ExecutorService threadPool;

    static {
        requestHandler = new RequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
        threadPool.execute(() -> {
            try {
                log.info("服务器接收到请求: {}", msg);
                Object result = requestHandler.handle(msg);
                ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }


}
