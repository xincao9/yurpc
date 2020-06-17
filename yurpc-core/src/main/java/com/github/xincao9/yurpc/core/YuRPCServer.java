/*
 * Copyright 2019 xincao9@gmail.com.
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
package com.github.xincao9.yurpc.core;

import com.github.xincao9.yurpc.core.impl.YuRPCServerImpl;
import com.github.xincao9.yurpc.core.config.ServerConfig;

/**
 * 服务组件
 *
 * @author xincao9@gmail.com
 */
public interface YuRPCServer {

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
     * @param discovery 服务发现组件
     * @return 服务端
     */
    static YuRPCServer defaultYuRPCServer(String filename, DiscoveryService discovery) throws RuntimeException {
        ServerConfig.init(filename);
        return new YuRPCServerImpl(discovery);
    }

    /**
     * 获得服务端
     *
     * @return 服务端
     */
    static YuRPCServer defaultYuRPCServer() {
        return YuRPCServer.defaultYuRPCServer("", null);
    }

    /**
     * 获得服务端
     *
     * @param port 端口
     * @param discoveryService 服务发现组件
     * @return
     */
    static YuRPCServer defaultYuRPCServer(Integer port, DiscoveryService discoveryService) {
        ServerConfig.port = port;
        return new YuRPCServerImpl(port, discoveryService);
    }

    /**
     * 获得服务端
     *
     * @param filename 配置文件名
     * @return 服务端
     */
    static YuRPCServer defaultYuRPCServer(String filename) {
        return YuRPCServer.defaultYuRPCServer(filename, null);
    }

    /**
     * 获得服务端
     *
     * @param discovery 服务发现组件
     * @return 服务端
     */
    static YuRPCServer defaultYuRPCServer(DiscoveryService discovery) {
        return YuRPCServer.defaultYuRPCServer("", discovery);
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

    /**
     * 修改器
     *
     * @param discoveryService 服务发现和注册组件
     */
    void setDiscoveryService(DiscoveryService discoveryService);
}
