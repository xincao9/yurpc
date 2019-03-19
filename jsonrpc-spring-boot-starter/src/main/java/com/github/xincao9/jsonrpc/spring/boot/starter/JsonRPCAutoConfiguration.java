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
package com.github.xincao9.jsonrpc.spring.boot.starter;

import com.github.xincao9.jsonrpc.core.client.ClientConfig;
import com.github.xincao9.jsonrpc.core.client.JsonRPCClient;
import com.github.xincao9.jsonrpc.core.client.JsonRPCClientImpl;
import com.github.xincao9.jsonrpc.core.constant.ClientConsts;
import com.github.xincao9.jsonrpc.core.constant.ServerConsts;
import com.github.xincao9.jsonrpc.core.server.JsonRPCServer;
import com.github.xincao9.jsonrpc.core.server.JsonRPCServerImpl;
import com.github.xincao9.jsonrpc.core.server.ServerConfig;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 自动配置类
 * 
 * @author xincao9@gmail.com
 */
@Configuration
public class JsonRPCAutoConfiguration implements InitializingBean {

    @Autowired
    private Environment environment;
    private boolean server;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonRPCClient jsonRPCClient() throws Throwable {
        Properties pros = new Properties();
        pros.setProperty(ClientConsts.DEFAULT_SERVER_LIST, environment.getProperty(ClientConsts.DEFAULT_SERVER_LIST));
        pros.setProperty(ClientConsts.CONNECTION_TIMEOUT_MS, environment.getProperty(ClientConsts.CONNECTION_TIMEOUT_MS));
        pros.setProperty(ClientConsts.INVOKE_TIMEOUT_MS, environment.getProperty(ClientConsts.INVOKE_TIMEOUT_MS));
        ClientConfig.init(pros);
        JsonRPCClient jsonRPCClient = new JsonRPCClientImpl();
        jsonRPCClient.start();
        return jsonRPCClient;
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonRPCServer jsonRPCServer () throws Throwable {
        if (server) {
            Properties pros = new Properties();
            pros.setProperty(ServerConsts.PORT, environment.getProperty(ServerConsts.PORT));
            pros.setProperty(ServerConsts.IO_THREAD_BOSS, environment.getProperty(ServerConsts.IO_THREAD_BOSS));
            pros.setProperty(ServerConsts.IO_THREAD_WORKER, environment.getProperty(ServerConsts.IO_THREAD_WORKER));
            ServerConfig.init(pros);
            JsonRPCServer jsonRPCServer = new JsonRPCServerImpl();
            jsonRPCServer.start();
            return jsonRPCServer;
        }
        return null;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

}
