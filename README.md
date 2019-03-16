# jsonrpc

### simple rpc scheme

> 使用时，仅需要添加自己实现了slf4j-api的log框架，强烈推荐使用logback

<pre>
package com.github.xincao9.jsonrpc;

import com.github.xincao9.jsonrpc.client.JsonRPCClient;
import com.github.xincao9.jsonrpc.server.SyncMethod;
import com.github.xincao9.jsonrpc.server.JsonRPCServer;
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
}</pre>