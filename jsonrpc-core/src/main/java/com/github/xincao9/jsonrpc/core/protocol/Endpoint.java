/*
 * Copyright 2018 xincao9@gmail.com.
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
package com.github.xincao9.jsonrpc.core.protocol;

import com.github.xincao9.jsonrpc.core.config.ServerConfig;
import com.github.xincao9.jsonrpc.core.util.HostUtils;
import java.util.Date;
import java.util.UUID;

/**
 * 端点
 * 
 * @author xincao9@gmail.com
 */
public class Endpoint {

    private String instanceId;
    private String host;
    private Integer port;
    private String name;
    private Date createTime;

    /**
     * 创建端点
     * 
     * @param name 服务名字
     * @return 端点
     */
    public static Endpoint create (String name) {
        Endpoint node = new Endpoint();
        node.setInstanceId(UUID.randomUUID().toString());
        node.setHost(HostUtils.getLocalAddress());
        node.setPort(ServerConfig.port);
        node.setName(name);
        node.setCreateTime(new Date());
        return node;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
