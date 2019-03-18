package com.github.xincao9.jsonrpc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.jsonrpc.common.Request;
import com.github.xincao9.jsonrpc.common.Response;
import com.github.xincao9.jsonrpc.constant.ResponseCode;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xincao9@gmail.com
 */
public class ClientInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInvocationHandler.class);

    private JsonRPCClient jsonRPCClient;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getTypeName()).append('.');
        sb.append(method.getName());
        Type retureType = method.getReturnType();
        Request request;
        if ("void".equalsIgnoreCase(retureType.getTypeName())) {
            request = Request.createRequest(Boolean.FALSE, method.getName(), args);
        } else {
            request = Request.createRequest(Boolean.TRUE, method.getName(), args);
        }
        Response response = jsonRPCClient.invoke(request);
        if (Objects.equals(response.getCode(), ResponseCode.OK) && !"void".equalsIgnoreCase(retureType.getTypeName())) {
            if (response.getData() == null) {
                return null;
            }
            Class clazz = Class.forName(retureType.getTypeName());
            return JSON.parseObject(JSONObject.toJSONString(response.getData(), SerializerFeature.DisableCircularReferenceDetect), clazz);
        }
        if (!Objects.equals(response.getCode(), ResponseCode.OK)) {
            LOGGER.error("request = {}, code = {}, msg = {}", request, response.getCode(), response.getMsg());
        }
        return null;
    }

    public <T> T proxy(Class<T> clazz) {
        if (clazz.isInterface() == false) {
            LOGGER.error("{} is not interface", clazz.getCanonicalName());
            return null;
        }
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    public void setJsonRPCClient(JsonRPCClient jsonRPCClient) {
        this.jsonRPCClient = jsonRPCClient;
    }

}

//            boolean isCollection = false;
//            for (Class c : clazz.getInterfaces()) {
//                if (c == Collection.class) {
//                    isCollection = true;
//                }
//            }
//            if (isCollection) {
//                return response.getData();
//            }
