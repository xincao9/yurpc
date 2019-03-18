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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.jsonrpc.common.Request;
import com.github.xincao9.jsonrpc.client.JsonRPCClient;
import com.github.xincao9.jsonrpc.server.SyncMethod;
import com.github.xincao9.jsonrpc.server.JsonRPCServer;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author xincao9@gmail.com
 */
public class JsonRPCServerTest {

    public JsonRPCServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public static class SayMethodImpl implements SyncMethod {

        @Override
        public Object exec(Request request) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
            return request.getParams()[0];
        }

        @Override
        public String getName() {
            return "perform";
        }
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

    @Test
    public void testPingMethod() throws Throwable {
        JsonRPCServer jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
        jsonRPCServer.register(new SayMethodImpl());
        jsonRPCServer.start();
        JsonRPCClient jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
        jsonRPCClient.start();
        SayService sayService = jsonRPCClient.proxy(SayService.class);
        for (int no = 0; no < 100; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Say say = new Say(no, value);
            System.out.println(sayService.perform(Collections.singletonMap(no, say)));
        }
        jsonRPCClient.shutdown();
        jsonRPCServer.shutdown();
    }
}
