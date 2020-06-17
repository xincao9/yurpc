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
package com.github.xincao9.yurpc.core.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer;
import com.github.xincao9.yurpc.core.MetricService;
import com.github.xincao9.yurpc.core.constant.MetricConsts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 指标服务
 *
 * @author xincao9@gmail.com
 */
public class MetricServiceImpl implements MetricService {

    private final MetricRegistry metricRegistry = new MetricRegistry();
    private final Map<String, Timer> timers = new ConcurrentHashMap();

    /**
     * 构造器
     */
    public MetricServiceImpl() {
        Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    /**
     * 获得计时信息
     *
     * @return 计时信息
     */
    @Override
    public List<Map<String, Object>> getTimers() {
        List<Map<String, Object>> rows = new ArrayList();
        timers.forEach((String method, Timer timer) -> {
            Map<String, Object> row = new HashMap();
            row.put(MetricConsts.METHOD, method);
            row.put(MetricConsts.COUNT, timer.getCount());
            row.put(MetricConsts.FIFTEEN_MINUTE_RATE, timer.getFifteenMinuteRate());
            row.put(MetricConsts.FIVE_MINUTE_RATE, timer.getFiveMinuteRate());
            row.put(MetricConsts.MEAN_RATE, timer.getMeanRate());
            row.put(MetricConsts.ONE_MINUTE_RATE, timer.getOneMinuteRate());
            rows.add(row);
        });
        return rows;
    }

    /**
     * 获得计时器
     *
     * @param name 方法名
     * @return 计时器
     */
    public Timer getTimerByName(String name) {
        if (timers.containsKey(name)) {
            return timers.get(name);
        }
        Timer timer = metricRegistry.timer(name);
        timers.put(name, timer);
        return timer;
    }

}
