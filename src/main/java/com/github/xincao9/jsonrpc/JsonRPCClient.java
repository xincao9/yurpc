/*
 * Copyright 2018 xingyunzhi.
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
package com.github.xincao9.jsonrpc;

import java.util.Map;

/**
 *
 * @author xincao9@gmail.com
 */
public interface JsonRPCClient {

    /**
     *
     * @param <T>
     * @param request
     * @return 
     * @throws Throwable
     */
    <T> Response<T> invoke(Request request) throws Throwable;

    /**
     *
     * @throws java.lang.Throwable
     */
    void start() throws Throwable;

    /**
     *
     * @throws java.lang.Throwable
     */
    void shutdown() throws Throwable;

    /**
     *
     * @return
     */
    static JsonRPCClient defaultJsonRPCClient() {
        return new JsonRPCClientImpl();
    }


    /**
     *
     * @return
     */
    Map<Long, Request> getRequests();
}
