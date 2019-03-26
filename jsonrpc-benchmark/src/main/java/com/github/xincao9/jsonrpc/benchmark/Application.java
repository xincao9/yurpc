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
package com.github.xincao9.jsonrpc.benchmark;

import com.github.xincao9.jsonrpc.benchmark.constant.ConfigConsts;
import com.github.xincao9.jsonrpc.benchmark.server.FibonacciSequenceServiceImpl;
import com.github.xincao9.jsonrpc.benchmark.server.PrimeNumberServiceImpl;
import com.github.xincao9.jsonrpc.benchmark.server.SleepServiceImpl;
import com.github.xincao9.jsonrpc.benchmark.server.StreamServiceImpl;
import com.github.xincao9.jsonrpc.core.JsonRPCClient;
import com.github.xincao9.jsonrpc.core.JsonRPCServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 * 
 * @author xincao9@gmail.com
 */
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static String role;
    private static JsonRPCClient jsonRPCClient;

    /**
     * 入口方法
     * 
     * @param args 参数
     * @throws Throwable 异常
     */
    public static void main(String... args) throws Throwable {
        role = System.getProperty(ConfigConsts.ROLE);
        if (ConfigConsts.PROVIDER.equalsIgnoreCase(role)) {
            LOGGER.info("bootstrap service provider");
            JsonRPCServer jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
            jsonRPCServer.register(new FibonacciSequenceServiceImpl());
            jsonRPCServer.register(new SleepServiceImpl());
            jsonRPCServer.register(new PrimeNumberServiceImpl());
            jsonRPCServer.register(new StreamServiceImpl());
            jsonRPCServer.start();
        } else if (ConfigConsts.CONSUMER.equalsIgnoreCase(role)) {
            LOGGER.info("bootstrap service consumer");
            jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
            jsonRPCClient.start();
            SpringApplication.run(Application.class, args);
        } else {
            LOGGER.warn("java -Drole=[provider | consumer] -jar jsonrpc-benchmark.jar");
        }
    }

    /**
     * 斐波那契服务
     * 
     * @return 斐波那契服务
     */
    @Bean
    public FibonacciSequenceService fibonacciSequenceService () {
        return jsonRPCClient.proxy(FibonacciSequenceService.class);
    }
    
    /**
     * 延时服务
     * 
     * @return 延时服务
     */
    @Bean
    public SleepService sleepService () {
        return jsonRPCClient.proxy(SleepService.class);
    }

    /**
     * 素数服务
     * 
     * @return 素数服务
     */
    @Bean
    public PrimeNumberService primeNumberService () {
        return jsonRPCClient.proxy(PrimeNumberService.class);
    }

    @Bean
    public StreamService streamService () {
        return jsonRPCClient.proxy(StreamService.class);
    }
}