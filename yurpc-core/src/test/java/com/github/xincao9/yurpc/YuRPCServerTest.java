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
package com.github.xincao9.yurpc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.yurpc.core.YuRPCClient;
import com.github.xincao9.yurpc.core.YuRPCServer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务组件测试
 *
 * @author xincao9@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class YuRPCServerTest {

    private YuRPCServer yuRPCServer;
    private YuRPCClient yuRPCClient;
    private SayService sayService;
    private AtomicInteger no = new AtomicInteger(0);

    /**
     * 启动服务组件
     *
     * @throws Throwable
     */
    @Setup
    public void setUp() throws Throwable {
        yuRPCServer = YuRPCServer.defaultYuRPCServer();
        yuRPCServer.register(new SayServiceImpl());
        yuRPCServer.start();
        yuRPCClient = YuRPCClient.defaultYuRPCClient();
        yuRPCClient.start();
        sayService = yuRPCClient.proxy(SayService.class);
    }

    /**
     * 关闭服务组件
     *
     * @throws Throwable
     */
    @TearDown
    public void tearDown() throws Throwable {
        yuRPCServer.shutdown();
        yuRPCClient.shutdown();
    }

    @Benchmark
    public void perform() {
        String value = RandomStringUtils.randomAscii(128);
        Say say = new Say(no.incrementAndGet(), value);
        System.out.println(sayService.perform(say));
    }

    /**
     * 客户端发送请求给服务端
     *
     * @throws Throwable
     */
    @Test
    public void testMethod() throws Throwable {
        Options opt = new OptionsBuilder()
                .include(getClass().getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
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

    public interface SayService {

        Say perform(Say say);
    }

    public static class SayServiceImpl implements SayService {

        @Override
        public Say perform(Say say) {
            return say;
        }

    }
}
