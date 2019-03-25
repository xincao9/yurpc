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
package com.github.xincao9.jsonrpc.sample.consumer;

import com.github.xincao9.jsonrpc.sample.Say;
import com.github.xincao9.jsonrpc.sample.SayService;
import com.github.xincao9.jsonrpc.spring.boot.starter.EnableJsonRPC;
import com.github.xincao9.jsonrpc.spring.boot.starter.JsonRPCAutowired;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * 服务消费者
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableJsonRPC(client = true)
public class ApplicationConsumer {

    @Autowired
    private Services services;

    @Service
    public static class Services {

        @JsonRPCAutowired
        private SayService sayService;
    }

    /**
     * 入口方法
     *
     * @param args 参数
     */
    public static void main(String... args) {
        SpringApplication.run(ApplicationConsumer.class, args);
    }

    /**
     * 启动时钩子
     *
     * @return
     */
    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            for (int no = 0; no < 100; no++) {
                String value = RandomStringUtils.randomAscii(128);
                Say say = new Say(no, value);
                System.out.println(services.sayService.perform(say));
            }
        };
    }

}
