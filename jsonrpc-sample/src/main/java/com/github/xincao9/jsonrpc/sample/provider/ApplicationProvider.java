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

import com.github.xincao9.jsonrpc.core.server.JsonRPCServer;
import com.github.xincao9.jsonrpc.sample.SayService;
import com.github.xincao9.jsonrpc.sample.SayServiceImpl;
import com.github.xincao9.jsonrpc.spring.boot.starter.EnableJsonRPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 服务提供者
 * 
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableJsonRPC(server = true)
public class ApplicationProvider {

    @Autowired
    private JsonRPCServer jsonRPCServer;

    /**
     * 招呼服务注册
     * 
     * @return 招呼服务
     */
    @Bean
    public SayService sayService () {
        SayService sayService = new SayServiceImpl();
        jsonRPCServer.register(sayService);
        return sayService;
    }

    /**
     * 入口方法
     * 
     * @param args 参数
     */
    public static void main(String... args) {
        SpringApplication.run(ApplicationProvider.class, args);
    }
}
