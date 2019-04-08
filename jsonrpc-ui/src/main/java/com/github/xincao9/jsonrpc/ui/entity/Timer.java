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
package com.github.xincao9.jsonrpc.ui.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时器
 * 
 * @author xincao9@gmail.com
 */
public class Timer implements Serializable {

    private Long id;
    private String host;
    private Integer port;
    private String method;
    private Long count;
    private Long oneMinuteRate;
    private Long fiveMinuteRate;
    private Long fifteenMinuteRate;
    private String ct;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getOneMinuteRate() {
        return oneMinuteRate;
    }

    public void setOneMinuteRate(Long oneMinuteRate) {
        this.oneMinuteRate = oneMinuteRate;
    }

    public Long getFiveMinuteRate() {
        return fiveMinuteRate;
    }

    public void setFiveMinuteRate(Long fiveMinuteRate) {
        this.fiveMinuteRate = fiveMinuteRate;
    }

    public Long getFifteenMinuteRate() {
        return fifteenMinuteRate;
    }

    public void setFifteenMinuteRate(Long fifteenMinuteRate) {
        this.fifteenMinuteRate = fifteenMinuteRate;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
