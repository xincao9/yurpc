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
package com.github.xincao9.jsonrpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author xincao9@gmail.com
 */
public class ClientInvocationHandlerTest {

    public ClientInvocationHandlerTest() {
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

    public class PingInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            StringBuilder sb = new StringBuilder();
            sb.append(method.getDeclaringClass().getTypeName()).append('.');
            sb.append(method.getName());
            System.out.println(sb.toString());
            Type retureType = method.getReturnType();
            System.out.println(retureType.getTypeName());
            Class cls = Class.forName(retureType.getTypeName());
            for (Class clazz : cls.getInterfaces()) {
                System.out.println(clazz.getCanonicalName());
            }
            return Collections.singletonList("pong");
        }

        public <T> T proxy(Class<T> clazz) {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        }
    }

    public static interface PingService {

        List<String> ping();
    }

    @Test
    public void testInvoke() {
        PingInvocationHandler invocationHandler = new PingInvocationHandler();
        PingService pingService = invocationHandler.proxy(PingService.class);
        pingService.ping();
    }

}
