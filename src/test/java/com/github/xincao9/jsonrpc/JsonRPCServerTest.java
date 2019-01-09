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
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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

    public static class PingMethodImpl implements SyncMethod {

        @Override
        public Object exec(Request request) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
            return request.getParams();
        }

        @Override
        public String getName() {
            return "ping";
        }
    }

    @Test
    public void testPingMethod() throws Throwable {
        int port = RandomUtils.nextInt(1025, 65535);
        JsonRPCServer jsonRPCServer = JsonRPCServer.defaultJsonRPCServer(port, 1, Runtime.getRuntime().availableProcessors());
        jsonRPCServer.register(new PingMethodImpl());
        jsonRPCServer.start();
        JsonRPCClient jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
        jsonRPCClient.start();
        for (int no = 0; no < 5000; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Request request = Request.createRequest(Boolean.TRUE, "ping", Collections.singletonList(value));
            request.setHost("127.0.0.1");
            request.setPort(port);
            Response<List<Object>> response = jsonRPCClient.invoke(request);
            System.out.println(JSONObject.toJSONString(response, SerializerFeature.DisableCircularReferenceDetect));
        }
        jsonRPCClient.shutdown();
        jsonRPCServer.shutdown();
    }
}
