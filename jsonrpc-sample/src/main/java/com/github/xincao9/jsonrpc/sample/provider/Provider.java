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
package com.github.xincao9.jsonrpc.sample.provider;

import com.github.xincao9.jsonrpc.core.JsonRPCServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务提供者
 * 
 * @author xincao9@gmail.com
 */
public class Provider {

    private static final Logger LOGGER = LoggerFactory.getLogger(Provider.class);

    /**
     * 入口方法
     * 
     * @param args 参数
     */
    public static void main(String... args) throws Throwable {
        JsonRPCServer jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
        jsonRPCServer.register(new SayServiceImpl());
        jsonRPCServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                jsonRPCServer.shutdown();
            } catch (Throwable ex) {
                LOGGER.error(ex.getMessage());
            }
        }));
    }
}
