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
package com.github.xincao9.jsonrpc.benchmark.server;

import com.github.xincao9.jsonrpc.benchmark.FibonacciSequenceService;

/**
 * 斐波那契服务
 * 
 * @author xincao9@gmail.com
 */
public class FibonacciSequenceServiceImpl implements FibonacciSequenceService {

    /**
     * 执行
     * 
     * @param n 项数
     * @return 结果
     */
    @Override
    public Integer perform(Integer n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return perform(n - 1) + perform (n - 2);
    }

}
