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
package com.github.xincao9.yurpc.spring.boot.starter;

import com.github.xincao9.yurpc.core.DiscoveryService;
import com.github.xincao9.yurpc.core.config.ClientConfig;
import com.github.xincao9.yurpc.core.YuRPCClient;
import com.github.xincao9.yurpc.core.impl.YuRPCClientImpl;
import com.github.xincao9.yurpc.core.constant.ClientConsts;
import com.github.xincao9.yurpc.core.constant.ServerConsts;
import com.github.xincao9.yurpc.core.YuRPCServer;
import com.github.xincao9.yurpc.core.impl.YuRPCServerImpl;
import com.github.xincao9.yurpc.core.config.ServerConfig;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 自动配置类
 *
 * @author xincao9@gmail.com
 */
public class YuRPCAutoConfiguration implements EnvironmentAware, DisposableBean, BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(YuRPCAutoConfiguration.class);

    private Environment environment;
    private Boolean server;
    private Boolean client;
    private YuRPCClient yuRPCClient;
    private YuRPCServer yuRPCServer;
    private YuRPCBeanPostProcessor yuRPCBeanPostProcessor;
    private DiscoveryService discoveryService;

    /**
     * 修改器
     *
     * @param environment 环境
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 自动扫描，注册服务组件
     *
     * @return 注册服务组件
     * @throws java.lang.Throwable  异常
     */
    @Bean
    public YuRPCBeanPostProcessor jsonRPCBeanPostProcessor() throws Throwable {
        if (yuRPCBeanPostProcessor != null) {
            return yuRPCBeanPostProcessor;
        }
        if (server || client) {
            yuRPCBeanPostProcessor = new YuRPCBeanPostProcessor(jsonRPCClient(discoveryService()), jsonRPCServer(discoveryService()));
            return yuRPCBeanPostProcessor;
        }
        return null;
    }

    /**
     * 释放资源
     *
     * @throws Exception 异常
     */
    @Override
    public void destroy() throws Exception {
        if (yuRPCClient != null) {
            try {
                yuRPCClient.shutdown();
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
            }
        }
        if (yuRPCServer != null) {
            try {
                yuRPCServer.shutdown();
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * 服务端角色
     *
     * @param server 状态
     */
    public void setServer(Boolean server) {
        this.server = server;
    }

    /**
     * 客户端角色
     *
     * @param client 状态
     */
    public void setClient(Boolean client) {
        this.client = client;
    }

    /**
     * 服务发现注册组件
     *
     * @return 服务发现注册组件
     * @throws java.lang.Throwable 异常
     */
    @Bean
    public DiscoveryService discoveryService() throws Throwable {
        if (discoveryService != null) {
            return discoveryService;
        }
        if ((client || server) && environment.containsProperty(ConfigConsts.DISCOVERY_ZOOKEEPER)) {
            discoveryService = new ZKDiscoveryServiceImpl(environment.getProperty(ConfigConsts.DISCOVERY_ZOOKEEPER));
            return discoveryService;
        }
        return null;
    }

    /**
     * 服务端
     *
     * @param discoveryService 服务发现注册组件
     * @return 服务端
     * @throws java.lang.Throwable 异常
     */
    @Bean
    public YuRPCServer jsonRPCServer(DiscoveryService discoveryService) throws Throwable {
        if (yuRPCServer != null) {
            return yuRPCServer;
        }
        if (server) {
            Properties pros = new Properties();
            if (environment.containsProperty(ServerConsts.PORT)) {
                pros.setProperty(ServerConsts.PORT, environment.getProperty(ServerConsts.PORT));
            }
            ServerConfig.init(pros);
            yuRPCServer = new YuRPCServerImpl();
            yuRPCServer.setDiscoveryService(discoveryService);
            yuRPCServer.start();
            return yuRPCServer;
        }
        return null;
    }

    /**
     * 客户端
     *
     * @param discoveryService 服务发现注册组件
     * @return 客户端
     * @throws java.lang.Throwable 异常
     */
    @Bean
    public YuRPCClient jsonRPCClient(DiscoveryService discoveryService) throws Throwable {
        if (yuRPCClient != null) {
            return yuRPCClient;
        }
        if (client) {
            Properties pros = new Properties();
            if (environment.containsProperty(ClientConsts.SERVER_LIST)) {
                pros.setProperty(ClientConsts.SERVER_LIST, environment.getProperty(ClientConsts.SERVER_LIST));
            }
            if (environment.containsProperty(ClientConsts.CONNECTION_TIMEOUT_MS)) {
                pros.setProperty(ClientConsts.CONNECTION_TIMEOUT_MS, environment.getProperty(ClientConsts.CONNECTION_TIMEOUT_MS));
            }
            if (environment.containsProperty(ClientConsts.INVOKE_TIMEOUT_MS)) {
                pros.setProperty(ClientConsts.INVOKE_TIMEOUT_MS, environment.getProperty(ClientConsts.INVOKE_TIMEOUT_MS));
            }
            ClientConfig.init(pros);
            yuRPCClient = new YuRPCClientImpl();
            yuRPCClient.setDiscoveryService(discoveryService);
            yuRPCClient.start();
            return yuRPCClient;
        }
        return null;
    }

    /**
     * 后置处理组件工厂
     *
     * @param beanFactory 容器上下文
     * @throws BeansException 组件异常
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (server || client) {
            try {
                beanFactory.addBeanPostProcessor(jsonRPCBeanPostProcessor());
            } catch (Throwable e) {
                throw new BeansException(e.getMessage()) {
                };
            }
        }
    }
}
