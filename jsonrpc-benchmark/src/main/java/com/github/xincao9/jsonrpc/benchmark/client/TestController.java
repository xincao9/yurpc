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
package com.github.xincao9.jsonrpc.benchmark.client;

import com.github.xincao9.jsonrpc.benchmark.FibonacciSequenceService;
import com.github.xincao9.jsonrpc.benchmark.PrimeNumberService;
import com.github.xincao9.jsonrpc.benchmark.SleepService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 * 
 * @author xincao9@gmail.com
 */
@RestController
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired(required = false)
    private FibonacciSequenceService fibonacciSequenceService;
    @Autowired(required = false)
    private SleepService sleepService;
    @Autowired(required = false)
    private PrimeNumberService primeNumberService;
    /**
     * 斐波那契
     * 
     * @return 随机项的结果
     */
    @GetMapping("/fibonacci_sequence")
    public ResponseEntity<Integer> fibonacciSequence() {
        if (fibonacciSequenceService == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            int n = RandomUtils.nextInt(0, 16);
            return ResponseEntity.ok().body(fibonacciSequenceService.perform(n));
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
        }
        return ResponseEntity.status(500).build();
    }

    /**
     * 阻塞
     * 
     * @return 阻塞时间（ms）
     */
    @GetMapping("/sleep")
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
     * 素数计算
     * 
     * @return 
     */
    @GetMapping("/prime_number")
    public ResponseEntity<Boolean> primeNumber () {
        if (primeNumberService == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            int n = RandomUtils.nextInt(1, 300);
            return ResponseEntity.ok().body(primeNumberService.perform(n));
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
        }
        return ResponseEntity.status(500).build();
    }
}
