/*
 * Copyright 2019 caoxin.
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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author xincao9@gmail.com
 */
@ConfigurationProperties(prefix = "jsonrpc.server")
public class JsonRPCServerProperties {

    private Integer port;
    private Integer ioThreadBoss;
    private Integer ioThreadWorker;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getIoThreadBoss() {
        return ioThreadBoss;
    }

    public void setIoThreadBoss(Integer ioThreadBoss) {
        this.ioThreadBoss = ioThreadBoss;
    }

    public Integer getIoThreadWorker() {
        return ioThreadWorker;
    }

    public void setIoThreadWorker(Integer ioThreadWorker) {
        this.ioThreadWorker = ioThreadWorker;
    }

}
