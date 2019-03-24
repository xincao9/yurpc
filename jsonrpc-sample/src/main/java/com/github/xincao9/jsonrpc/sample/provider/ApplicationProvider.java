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

import com.github.xincao9.jsonrpc.spring.boot.starter.EnableJsonRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务提供者
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableJsonRPC(server = true)
public class ApplicationProvider {

    /**
     * 入口方法
     *
     * @param args 参数
     */
    public static void main(String... args) {
        SpringApplication.run(ApplicationProvider.class, args);
    }
}
