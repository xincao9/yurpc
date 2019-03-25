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

package com.github.xincao9.jsonrpc.spring.boot.starter;

import com.github.xincao9.jsonrpc.core.client.JsonRPCClient;
import com.github.xincao9.jsonrpc.core.server.JsonRPCServer;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 自动扫描，注册服务组件
 * 
 * @author xincao9@gmail.com
 */
public class JsonRPCBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonRPCBeanPostProcessor.class);

    private final JsonRPCServer jsonRPCServer;
    private final JsonRPCClient jsonRPCClient;

    public JsonRPCBeanPostProcessor(JsonRPCClient jsonRPCClient, JsonRPCServer jsonRPCServer) {
        this.jsonRPCClient = jsonRPCClient;
        this.jsonRPCServer = jsonRPCServer;
    }

    /**
     * 初始化前执行
     * 
     * @param bean 实例
     * @param beanName 名字
     * @return
     * @throws BeansException 异常
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (this.jsonRPCServer != null && bean != null && bean.getClass().isAnnotationPresent(JsonRPCService.class)) {
            this.jsonRPCServer.register(bean);
            LOGGER.info("register jsonrpc service = {}", beanName);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    /**
     * 初始化后执行
     * 
     * @param bean 实例
     * @param beanName 名字
     * @return
     * @throws BeansException 异常
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.jsonRPCClient != null && bean != null) {
            Class clazz = bean.getClass();
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(JsonRPCAutowired.class)) {
                        Object obj = this.jsonRPCClient.proxy(field.getType());
                        try {
                            field.setAccessible(true);
                            field.set(bean, obj);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            LOGGER.error(ex.getMessage());
                            throw new BeansException(ex.getMessage()) {
                            };
                        }
                        LOGGER.info("service reference beanName = {}, field = {}", beanName, field.getName());
                    }
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}
