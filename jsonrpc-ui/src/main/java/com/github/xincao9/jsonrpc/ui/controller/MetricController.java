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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

    private Map<String, Map<String, AtomicLong>> m1 = new ConcurrentHashMap();
    private Map<String, Map<String, AtomicLong>> m5 = new ConcurrentHashMap();
    private Map<String, Map<String, AtomicLong>> m15 = new ConcurrentHashMap();

    @PostConstruct
    public void initMethod () {
        refresh();
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void refresh () {
        List<Endpoint> endpoints = discoveryService.query(MetricService.class.getTypeName());
        if (endpoints == null || endpoints.isEmpty()) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
        String ct = sdf.format(new Date());
        for (Endpoint endpoint : endpoints) {
            Map<String, Map<String, Object>> map = getTimerByHostAndPort(endpoint.getHost(), endpoint.getPort());
            if (map == null || map.isEmpty()) {
                continue;
            }
            for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
                String method = entry.getKey();
                Map<String, Object> obj = entry.getValue();
                JSONObject v0 = JSONObject.parseObject(JSONObject.toJSONString(endpoint));
                JSONObject v1 = JSONObject.parseObject(JSONObject.toJSONString(obj));
                v0.putAll(v1);
                method = StringUtils.substringAfterLast(method, " ");
                if (!m1.containsKey(method)) {
                    m1.put(method, new LinkedHashMap());
                }
                if (!m5.containsKey(method)) {
                    m5.put(method, new LinkedHashMap());
                }
                if (!m15.containsKey(method)) {
                    m15.put(method, new LinkedHashMap());
                }
                Map<String, AtomicLong> tc1 = m1.get(method);
                if (!tc1.containsKey(ct)) {
                    tc1.put(ct, new AtomicLong(0));
                }
                AtomicLong oneMinuteRate = tc1.get(ct);
                oneMinuteRate.addAndGet(v0.getDouble("oneMinuteRate").longValue());

                Map<String, AtomicLong> tc5 = m5.get(method);
                if (!tc5.containsKey(ct)) {
                    tc5.put(ct, new AtomicLong(0));
                }
                AtomicLong fiveMinuteRate = tc5.get(ct);
                fiveMinuteRate.addAndGet(v0.getDouble("fiveMinuteRate").longValue());
                Map<String, AtomicLong> tc15 = m15.get(method);
                if (!tc15.containsKey(ct)) {
                    tc15.put(ct, new AtomicLong(0));
                }
                AtomicLong fifteenMinuteRate = tc15.get(ct);
                fifteenMinuteRate.addAndGet(v0.getDouble("fifteenMinuteRate").longValue());
            }
        }
    }

    /**
     * 获得计数器信息列表
     * 
     * @return 计数器信息列表
     */
    @GetMapping("timer")
    public ResponseEntity<List<Map<String, Object>>> timer() {
        if (m1.isEmpty()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        List<Map<String, Object>> timer = new ArrayList();
        for (Entry<String, Map<String, AtomicLong>> e1 : m1.entrySet()) {
            Map<String, Object> map = new HashMap();
            map.put("label", e1.getKey());
            List<String> x = new ArrayList(e1.getValue().size());
            List<Long> y1 = new ArrayList(e1.getValue().size());
            List<Long> y5 = new ArrayList(e1.getValue().size());
            List<Long> y15 = new ArrayList(e1.getValue().size());
            for (Entry<String, AtomicLong> e2 : e1.getValue().entrySet()) {
                x.add(e2.getKey());
                y1.add(e2.getValue().get());
                y5.add(m5.get(e1.getKey()).get(e2.getKey()).get());
                y15.add(m15.get(e1.getKey()).get(e2.getKey()).get());
            }
            map.put("x", x);
            map.put("y1", y1);
            map.put("y5", y5);
            map.put("y15", y15);
            timer.add(map);
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
