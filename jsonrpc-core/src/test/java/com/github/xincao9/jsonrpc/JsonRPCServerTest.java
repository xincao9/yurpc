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
package com.github.xincao9.jsonrpc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.jsonrpc.core.JsonRPCClient;
import com.github.xincao9.jsonrpc.core.JsonRPCServer;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 服务组件测试
 * 
 * @author xincao9@gmail.com
 */
public class JsonRPCServerTest {

    private static JsonRPCServer jsonRPCServer;

    /**
     * 启动服务组件
     * 
     * @throws Throwable 
     */
    @BeforeClass
    public static void setUpClass() throws Throwable {
        jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
        jsonRPCServer.register(new SayServiceImpl());
        jsonRPCServer.start();
    }

    /**
     * 关闭服务组件
     * 
     * @throws Throwable 
     */
    @AfterClass
    public static void tearDownClass() throws Throwable {
        jsonRPCServer.shutdown();
    }

    public static class Say {

        private Integer id;
        private String body;

        public Say(Integer id, String body) {
            this.id = id;
            this.body = body;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
        }

    }

    public static interface SayService {

        Map<Integer, Say> perform(Map<Integer, Say> saies);
    }

    /**
     * 客户端发送请求给服务端
     * 
     * @throws Throwable 
     */
    @Test
    public void testMethod() throws Throwable {
        JsonRPCClient jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
        jsonRPCClient.start();
        SayService sayService = jsonRPCClient.proxy(SayService.class);
        for (int no = 0; no < 100; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Say say = new Say(no, value);
            System.out.println(sayService.perform(Collections.singletonMap(no, say)));
        }
        jsonRPCClient.shutdown();
    }

    public static class SayServiceImpl implements SayService {

        @Override
        public Map<Integer, Say> perform(Map<Integer, Say> saies) {
            return saies;
        }

    }
}
