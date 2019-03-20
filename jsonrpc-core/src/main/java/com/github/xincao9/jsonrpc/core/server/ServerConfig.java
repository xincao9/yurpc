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
package com.github.xincao9.jsonrpc.core.server;

import com.github.xincao9.jsonrpc.core.constant.ServerConsts;
import com.github.xincao9.jsonrpc.core.util.PropertiesUtils;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务配置类
 *
 * @author xincao9@gmail.com
 */
public class ServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfig.class);

    public static Integer port;
    public static Integer ioThreadBoss;
    public static Integer ioThreadWorker;

    /**
     * 初始化服务组件配置
     *
     * @param filename 配置文件名
     */
    public static void init(String filename) {
        Properties pros = PropertiesUtils.read(filename, ServerConsts.DEFAULT_CONFIG_FILENAME);
        init(pros);
    }

    /**
     * 初始化服务组件配置
     *
     * @param pros 属性文件
     */
    public static void init(Properties pros) {
        port = Integer.valueOf(String.valueOf(pros.getProperty(ServerConsts.PORT, ServerConsts.DEFAULT_PORT)));
        ioThreadBoss = Integer.valueOf(String.valueOf(pros.getProperty(ServerConsts.IO_THREAD_BOSS, ServerConsts.DEFAULT_IO_THREAD_BOSS)));
        ioThreadWorker = Integer.valueOf(String.valueOf(pros.getProperty(ServerConsts.IO_THREAD_WORKER, String.valueOf(ServerConsts.DEFAULT_IO_THREAD_WORKER))));
        if (port <= 0 || port > 65535) {
            port = Integer.valueOf(ServerConsts.DEFAULT_PORT);
        }
        if (ioThreadBoss <= 0 || ioThreadBoss > 1024) {
            ioThreadBoss = Integer.valueOf(ServerConsts.DEFAULT_IO_THREAD_BOSS);
        }
        if (ioThreadWorker <= 0) {
            ioThreadWorker = ServerConsts.DEFAULT_IO_THREAD_WORKER;
        }
    }
}
