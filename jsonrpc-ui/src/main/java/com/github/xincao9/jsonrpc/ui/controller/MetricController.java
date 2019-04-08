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
package com.github.xincao9.jsonrpc.ui.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.jsonrpc.core.DiscoveryService;
import com.github.xincao9.jsonrpc.core.JsonRPCClient;
import com.github.xincao9.jsonrpc.core.MetricService;
import com.github.xincao9.jsonrpc.core.constant.MetricConsts;
import com.github.xincao9.jsonrpc.core.constant.ResponseCode;
import com.github.xincao9.jsonrpc.core.protocol.Endpoint;
import com.github.xincao9.jsonrpc.core.protocol.Request;
import com.github.xincao9.jsonrpc.core.protocol.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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

    private final List<Map<String, Object>> timers = new CopyOnWriteArrayList();

    @PostConstruct
    public void initMethod() {
        refresh();
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void refresh() {
        List<Endpoint> endpoints = discoveryService.query(MetricService.class.getTypeName());
        if (endpoints == null || endpoints.isEmpty()) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
        String createTime = sdf.format(new Date());
        endpoints.stream()
                .map((endpoint) -> getTimerByHostAndPort(endpoint.getHost(), endpoint.getPort()))
                .filter((rows) -> !(rows == null || rows.isEmpty()))
                .forEachOrdered((rows) -> {
                    rows.stream().map((row) -> {
                        row.put("createTime", createTime);
                        return row;
                    }).forEachOrdered((row) -> {
                        timers.add(row);
                    });
                });
    }

    /**
     * 获得计数器信息列表
     *
     * @return 计数器信息列表
     */
    @GetMapping("timers")
    public ResponseEntity<List<Map<String, Object>>> timers() {
        if (timers.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        List<String> x = new ArrayList(timers.size());
        timers.stream().map((t) -> String.valueOf(t.get("createTime"))).forEachOrdered((t) -> {
            if (!x.contains(t)) {
                x.add(t);
            }
        });
        Map<String, List<Map<String, Object>>> methodGroup = timers.stream().collect(Collectors.groupingBy((t) -> {
            return String.valueOf(t.get(MetricConsts.METHOD));
        }));
        List<Map<String, Object>> resp = new ArrayList();
        AtomicInteger no = new AtomicInteger(0);
        methodGroup.entrySet().stream().forEachOrdered((mg) -> {
            Map<String, Object> obj = new HashMap();
            obj.put("no", no.incrementAndGet());
            obj.put("x", x);
            String key = mg.getKey();
            List<Map<String, Object>> value = mg.getValue();
            obj.put(MetricConsts.METHOD, StringUtils.substringAfterLast(key, " "));
            List<Long> m1 = new ArrayList();
            List<Long> m2 = new ArrayList();
            List<Long> m3 = new ArrayList();
            Map<String, List<Map<String, Object>>> timeGroup = value.stream().collect(Collectors.groupingBy((o) -> {
                return String.valueOf(o.get("createTime"));
            }));
            x.stream().map((time) -> timeGroup.get(time)).filter((group) -> group != null && !group.isEmpty()).forEachOrdered((group) -> {
                long m1c = 0;
                long m2c = 0;
                long m3c = 0;
                for (Map<String, Object> m : group) {
                    m1c += Double.valueOf(String.valueOf(m.get(MetricConsts.ONE_MINUTE_RATE))).longValue();
                    m2c += Double.valueOf(String.valueOf(m.get(MetricConsts.FIVE_MINUTE_RATE))).longValue();
                    m3c += Double.valueOf(String.valueOf(m.get(MetricConsts.FIFTEEN_MINUTE_RATE))).longValue();
                }
                m1.add(m1c);
                m2.add(m2c);
                m3.add(m3c);
            });
            obj.put("m1", m1);
            obj.put("m2", m2);
            obj.put("m3", m3);
            resp.add(obj);
        });
        return ResponseEntity.ok(resp);
    }

    /**
     * 获得计数器信息
     *
     * @param host 主机
     * @param port 端口
     * @return 计数器信息
     */
    private List<Map<String, Object>> getTimerByHostAndPort(String host, Integer port) {
        StringBuilder method = new StringBuilder();
        method.append(MetricService.class.getTypeName())
                .append('.')
                .append("getTimers");
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
                return JSON.parseObject(JSONObject.toJSONString(response.getData(), SerializerFeature.DisableCircularReferenceDetect), List.class);
            }
        } catch (Throwable ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }
}
