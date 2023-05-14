package com.example.server.netty;

import com.example.common.entity.RpcRequest;
import com.example.common.entity.RpcResponse;
import com.example.common.enumeration.ResponseEnum;
import com.example.common.util.ThreadPoolFactory;
import com.example.server.provider.ServiceProvider;
import com.example.server.provider.ServiceProviderImpl;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final ExecutorService threadPool=ThreadPoolFactory.createDefaultThreadPool("netty-server-handler");
    private static final ServiceProvider serviceProvider=new ServiceProviderImpl();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) {
        threadPool.execute(() -> {
            try {
                log.info("服务器接收到请求: {}", rpcRequest);
                Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
                Object result = method.invoke(service, rpcRequest.getParameters());
                log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
                ctx.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.info("服务:{} 调用方法:{}失败", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
                ctx.writeAndFlush(RpcResponse.fail(ResponseEnum.METHOD_NOT_FOUND, rpcRequest.getRequestId()));
            } finally {
                ReferenceCountUtil.release(rpcRequest);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("处理过程调用时有错误发生:",cause);
        ctx.close();
    }


}
