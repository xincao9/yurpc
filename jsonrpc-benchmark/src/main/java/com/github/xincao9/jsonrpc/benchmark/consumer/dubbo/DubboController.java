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
package com.github.xincao9.jsonrpc.benchmark.consumer.dubbo;

import com.github.xincao9.jsonrpc.benchmark.SleepService;
import com.github.xincao9.jsonrpc.benchmark.StreamService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 * 
 * @author xincao9@gmail.com
 */
@RestController
@RequestMapping("/dubbo")
public class DubboController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboController.class);

    @Reference(version = "1.0.0")
    private SleepService sleepService;
    @Reference(version = "1.0.0")
    private StreamService streamService;

    /**
     * 阻塞
     * 
     * @return 阻塞时间（ms）
     */
    @GetMapping("sleep")
    public ResponseEntity<Integer> sleep() {
        if (sleepService == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            int ms = RandomUtils.nextInt(0, 50);
            sleepService.perform(ms);
            return ResponseEntity.ok().body(ms);
        } catch (Throwable e) {
            LOGGER.info(e.getMessage());
        }
        return ResponseEntity.status(500).build();
    }

    /**
     * 流
     * 
     * @return 流
     */
    @GetMapping("stream")
    public ResponseEntity<String> stream () {
        if (streamService == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            return ResponseEntity.ok().body(streamService.perform(RandomStringUtils.randomAscii(128)));
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
        }
        return ResponseEntity.status(500).build();
    }
}
