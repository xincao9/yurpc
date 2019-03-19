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

import com.github.xincao9.jsonrpc.core.constant.ServerConfigConsts;
import com.github.xincao9.jsonrpc.core.util.PropertiesUtils;
import java.io.IOException;
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
     * 初始化
     * 
     * @param filename 配置文件名
     * @return 初始化状态
     */
    public static Boolean init(String filename) {
        try {
            Properties pros = PropertiesUtils.read(filename, ServerConfigConsts.DEFAULT_CONFIG_FILENAME);
            port = Integer.valueOf(String.valueOf(pros.getProperty(ServerConfigConsts.PORT, ServerConfigConsts.DEFAULT_PORT)));
            ioThreadBoss = Integer.valueOf(String.valueOf(pros.getProperty(ServerConfigConsts.IO_THREAD_BOSS, ServerConfigConsts.DEFAULT_IO_THREAD_BOSS)));
            ioThreadWorker = Integer.valueOf(String.valueOf(pros.getProperty(ServerConfigConsts.IO_THREAD_WORKER, ServerConfigConsts.DEFAULT_IO_THREAD_WORKER)));
            if (port <= 0 || port > 65535) {
                port = Integer.valueOf(ServerConfigConsts.DEFAULT_PORT);
            }
            if (ioThreadBoss <= 0 || ioThreadBoss > 4) {
                ioThreadBoss = Integer.valueOf(ServerConfigConsts.DEFAULT_IO_THREAD_BOSS);
            }
            if (ioThreadWorker <= 0) {
                ioThreadWorker = Integer.valueOf(ServerConfigConsts.DEFAULT_IO_THREAD_WORKER);
            }
            return true;
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage());
        }
        return false;

    }
}
