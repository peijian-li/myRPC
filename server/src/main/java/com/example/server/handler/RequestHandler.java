package com.example.server.handler;

import com.example.common.entity.RpcRequest;
import com.example.common.entity.RpcResponse;
import com.example.common.enumeration.ResponseEnum;
import com.example.server.provider.ServiceProvider;
import com.example.server.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider=new ServiceProviderImpl();

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseEnum.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }

}

