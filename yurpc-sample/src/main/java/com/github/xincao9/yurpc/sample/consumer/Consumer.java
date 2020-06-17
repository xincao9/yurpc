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
package com.github.xincao9.yurpc.sample.consumer;

import com.github.xincao9.yurpc.core.YuRPCClient;
import com.github.xincao9.yurpc.sample.Say;
import com.github.xincao9.yurpc.sample.SayService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务消费者
 *
 * @author xincao9@gmail.com
 */
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    /**
     * 入口方法
     *
     * @param args 参数
     * @throws Throwable 异常
     */
    public static void main (String... args) throws Throwable {
        YuRPCClient yuRPCClient = YuRPCClient.defaultJsonRPCClient();
        yuRPCClient.start();
        SayService sayService = yuRPCClient.proxy(SayService.class);
        for (int no = 0; no < 100; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Say say = new Say(no, value);
            System.out.println(sayService.perform(say));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                yuRPCClient.shutdown();
            } catch (Throwable ex) {
                LOGGER.error(ex.getMessage());
            }
        }));
    }
}
