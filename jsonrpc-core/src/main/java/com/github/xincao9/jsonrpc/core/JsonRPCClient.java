/*
 * Copyright 2018 xincao9@gmail.com.
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
package com.github.xincao9.jsonrpc.core;

import com.github.xincao9.jsonrpc.core.config.ClientConfig;
import com.github.xincao9.jsonrpc.core.impl.JsonRPCClientImpl;
import com.github.xincao9.jsonrpc.core.protocol.Request;
import com.github.xincao9.jsonrpc.core.protocol.Response;
import java.util.Map;

/**
 * 客户端
 * 
 * @author xincao9@gmail.com
 */
public interface JsonRPCClient {

    /**
     * 调用方法
     * 
     * @param <T> 类型
     * @param request 请求
     * @return 调用结果
     * @throws Throwable 异常
     */
    <T> Response<T> invoke(Request request) throws Throwable;

    /**
     * 启动
     * 
     * @throws java.lang.Throwable 异常
     */
    void start() throws Throwable;

    /**
     * 关闭
     * 
     * @throws java.lang.Throwable 异常
     */
    void shutdown() throws Throwable;

    /**
     * 获得客户端
     * 
     * @param filename 配置文件
     * @param discoveryService 服务组件
     * @return 客户端
     */
    static JsonRPCClient defaultJsonRPCClient(String filename, DiscoveryService discoveryService) {
        ClientConfig.init(filename);
        return new JsonRPCClientImpl(discoveryService);
    }

    /**
     * 获得客户端
     * 
     * @return 客户端
     */
    static JsonRPCClient defaultJsonRPCClient() {
        return defaultJsonRPCClient("", null);
    }
    
    /**
     * 获得客户端
     * 
     * @param discoveryService 服务组件
     * @return 客户端
     */
    static JsonRPCClient defaultJsonRPCClient(DiscoveryService discoveryService) {
        return defaultJsonRPCClient("", discoveryService);
    }

    /**
     * 获得请求对象容器
     * 
     * @return 请求对象容器
     */
    Map<Long, Request> getRequests();

    /**
     * 获得接口的代理
     * 
     * @param <T> 类型
     * @param clazz 接口
     * @return 代理对象
     */
    <T> T proxy(Class<T> clazz);
}
