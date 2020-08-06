/*
 * Copyright 2020 xincao9@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xincao9.yurpc.core.impl;

import com.github.xincao9.yurpc.core.YuRPCClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.yurpc.core.protocol.Request;
import com.github.xincao9.yurpc.core.protocol.Response;
import com.github.xincao9.yurpc.core.constant.ResponseCode;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端调用代理
 *
 * @author xincao9@gmail.com
 */
public class ClientInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInvocationHandler.class);

    private YuRPCClient yuRPCClient;
    private final Map<Class, Object> proxies = new ConcurrentHashMap();

    /**
     * 代理方法调用
     *
     * @param proxy 代理对象
     * @param method 调用的方法
     * @param args 调用方法的参数
     * @return 调用方法的结果
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getTypeName()).append('.');
        sb.append(method.getName());
        Type returnType = method.getReturnType();
        Request request;
        String[] paramTypes = null;
        Class<?>[] clazzParamTypes = method.getParameterTypes();
        if (clazzParamTypes != null && clazzParamTypes.length > 0) {
            paramTypes = new String[clazzParamTypes.length];
            for (int i = 0; i < clazzParamTypes.length; i++) {
               paramTypes[i] = clazzParamTypes[i].getTypeName();
            }
        }
        if ("void".equalsIgnoreCase(returnType.getTypeName())) {
            request = Request.createRequest(Boolean.FALSE, sb.toString(), args);
        } else {
            request = Request.createRequest(Boolean.TRUE, sb.toString(), args);
        }
        request.setParamTypes(paramTypes);
        long startTime = System.currentTimeMillis();
        Response response = yuRPCClient.invoke(request);
        if (response == null) {
            return null;
        }
        LOGGER.debug("requestId = {}, invoke costTime = {}", request.getId(), System.currentTimeMillis() - startTime);
        LOGGER.debug("requestId = {}, c to s costTime = {} ms, s to c costTime {} ms", request.getId(), response.getCreateTime() - request.getCreateTime(), System.currentTimeMillis() - response.getCreateTime());
        if (Objects.equals(response.getCode(), ResponseCode.OK) && !"void".equalsIgnoreCase(returnType.getTypeName())) {
            if (response.getData() == null) {
                return null;
            }
            Class clazz = Class.forName(returnType.getTypeName());
            return JSON.parseObject(JSONObject.toJSONString(response.getData(), SerializerFeature.DisableCircularReferenceDetect), clazz);
        }
        if (!Objects.equals(response.getCode(), ResponseCode.OK)) {
            LOGGER.error("request = {}, code = {}, msg = {}", request, response.getCode(), response.getMsg());
            throw new RuntimeException(response.getMsg());
        }
        return null;
    }

    /**
     * 获得接口代理对象
     *
     * @param <T> 接口类型
     * @param clazz 接口
     * @return 接口的代理类
     */
    public <T> T proxy(Class<T> clazz) {
        if (!clazz.isInterface()) {
            LOGGER.error("{} is not interface", clazz.getCanonicalName());
            return null;
        }
        if (proxies.containsKey(clazz)) {
            return (T) proxies.get(clazz);
        }
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        proxies.put(clazz, proxy);
        return (T) proxy;
    }

    /**
     * 修改器
     *
     * @param yuRPCClient 客户端
     */
    public void setYuRPCClient(YuRPCClient yuRPCClient) {
        this.yuRPCClient = yuRPCClient;
    }

}
