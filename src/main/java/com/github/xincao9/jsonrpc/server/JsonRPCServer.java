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
package com.github.xincao9.jsonrpc.server;

/**
 * 服务组件
 *
 * @author xincao9@gmail.com
 */
public interface JsonRPCServer {

    /**
     * 启动
     *
     * @throws java.lang.Throwable
     */
    void start() throws Throwable;

    /**
     * 关闭
     *
     * @throws java.lang.Throwable
     */
    void shutdown() throws Throwable;

    /**
     * 获得服务端
     *
     * @param filename 配置文件名
     * @return 服务端
     */
    static JsonRPCServer defaultJsonRPCServer(String filename) throws RuntimeException {
        if (ServerConfig.init(filename) == false) {
            throw new RuntimeException("jsonrpc server bootstrap failure");
        }
        return new JsonRPCServerImpl();
    }

    /**
     * 获得服务端
     *
     * @return 服务端
     */
    static JsonRPCServer defaultJsonRPCServer() {
        return defaultJsonRPCServer("");
    }

    /**
     * 服务注册
     *
     * @param <T> 组建类型
     * @param obj 服务组件
     */
    <T> void register(T obj);

    /**
     * 获取组建
     *
     * @param name 组建类型名
     * @return 服务组件
     */
    Object getBean(String name);
}
