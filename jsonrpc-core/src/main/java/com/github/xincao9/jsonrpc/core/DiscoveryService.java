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
package com.github.xincao9.jsonrpc.core;

import com.github.xincao9.jsonrpc.core.protocol.Endpoint;
import java.util.List;

/**
 * 服务注册与发现组件
 * 
 * @author xincao9@gmail.com
 */
public interface DiscoveryService {

    /**
     * 注册
     * 
     * @param node 服务信息
     */
    void register (Endpoint node);

    /**
     * 获取注册表
     * 
     * @param service 服务名字（接口名）
     * @return 节点列表
     */
    List<Endpoint> query (String service);

    /**
     * 续约
     * 
     * @param instanceId 当前实例的id
     */
    void renew (String instanceId);

    /**
     * 取消
     * 
     * @param instanceId 当前实例的id
     */
    void cancel (String instanceId);
}
