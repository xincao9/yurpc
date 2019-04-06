/*
 * Copyright 2019 Personal.
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
package com.github.xincao9.jsonrpc.ui.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.jsonrpc.core.DiscoveryService;
import com.github.xincao9.jsonrpc.core.JsonRPCClient;
import com.github.xincao9.jsonrpc.core.MetricService;
import com.github.xincao9.jsonrpc.core.constant.ResponseCode;
import com.github.xincao9.jsonrpc.core.protocol.Endpoint;
import com.github.xincao9.jsonrpc.core.protocol.Request;
import com.github.xincao9.jsonrpc.core.protocol.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 度量
 * 
 * @author xincao9@gmail.com
 */
@RestController
@RequestMapping("/metric")
public class MetricController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricController.class);

    @Autowired
    private DiscoveryService discoveryService;
    @Autowired
    private JsonRPCClient jsonRPCClient;

    /**
     * 获得计数器信息列表
     * 
     * @return 计数器信息列表
     */
    @GetMapping("timer")
    public ResponseEntity<List<Map<String, Object>>> timer() {
        List<Endpoint> endpoints = discoveryService.query(MetricService.class.getTypeName());
        if (endpoints == null || endpoints.isEmpty()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        List<Map<String, Object>> timer = new ArrayList();
        int no = 1;
        for (Endpoint endpoint : endpoints) {
            Map<String, Map<String, Object>> map = getTimerByHostAndPort(endpoint.getHost(), endpoint.getPort());
            for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
                String method = entry.getKey();
                Map<String, Object> obj = entry.getValue();
                JSONObject v0 = JSONObject.parseObject(JSONObject.toJSONString(endpoint));
                JSONObject v1 = JSONObject.parseObject(JSONObject.toJSONString(obj));
                v0.putAll(v1);
                v0.put("method", StringUtils.substringAfterLast(method, " "));
                v0.put("no", no++);
                timer.add(v0);
            }
        }
        return ResponseEntity.ok(timer);
    }

    /**
     * 获得计数器信息
     *
     * @param host 主机
     * @param port 端口
     * @return 计数器信息
     */
    private Map<String, Map<String, Object>> getTimerByHostAndPort(String host, Integer port) {
        StringBuilder method = new StringBuilder();
        method.append(MetricService.class.getTypeName())
                .append('.')
                .append("timer");
        Request request = Request.createRequest(Boolean.TRUE, method.toString());
        request.setDirect(Boolean.TRUE);
        request.setHost(host);
        request.setPort(port);
        try {
            Response response = jsonRPCClient.invoke(request);
            if (Objects.equals(response.getCode(), ResponseCode.OK)) {
                if (response.getData() == null) {
                    return null;
                }
                return JSON.parseObject(JSONObject.toJSONString(response.getData(), SerializerFeature.DisableCircularReferenceDetect), Map.class);
            }
        } catch (Throwable ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }
}
