/*
 * Copyright 2018 xingyunzhi.
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
package com.github.xincao9.jsonrpc;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author xincao9@gmail.com
 */
public class Request {

    private Boolean requestType; // request with/without return value
    private Boolean eventType; // whether the request is an event message
    private Long id; // request id
    private List<Object> params;
    private static final AtomicLong COUNTER = new AtomicLong(0);
    private String method;
    private Long createTime;
    private String host;
    private int port;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private Response response;
    private Boolean sendOk;

    /**
     *
     * @param requestType
     * @param method
     * @param params
     * @return
     */
    public static Request createRequest(Boolean requestType, String method, List<Object> params) {
        Request request = new Request();
        request.setRequestType(requestType);
        request.setEventType(false);
        request.setId(COUNTER.getAndIncrement());
        request.setParams(params);
        request.setMethod(method);
        request.setCreateTime(System.currentTimeMillis());
        return request;
    }

    /**
     *
     * @param method
     * @return
     */
    public static Request createEvent(String method) {
        Request request = new Request();
        request.setRequestType(false);
        request.setEventType(true);
        request.setId(COUNTER.getAndIncrement());
        request.setMethod(method);
        request.setCreateTime(System.currentTimeMillis());
        return request;
    }

    /**
     *
     * @param <T>
     * @param timeout
     * @param timeUnit
     * @return
     * @throws InterruptedException
     */
    public <T> Response<T> waitResponse(int timeout, TimeUnit timeUnit) throws InterruptedException {
        this.countDownLatch.await(timeout, timeUnit);
        return response;
    }

    /**
     *
     * @param response
     */
    public void putResponse(Response response) {
        this.response = response;
        this.countDownLatch.countDown();
    }

    public Boolean getRequestType() {
        return requestType;
    }

    public void setRequestType(Boolean requestType) {
        this.requestType = requestType;
    }

    public Boolean getEventType() {
        return eventType;
    }

    public void setEventType(Boolean eventType) {
        this.eventType = eventType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Boolean getSendOk() {
        return sendOk;
    }

    public void setSendOk(Boolean sendOk) {
        this.sendOk = sendOk;
    }

    @Override
    public String toString() {
        return "Request{" + "requestType=" + requestType + ", eventType=" + eventType + ", id=" + id + ", params=" + params + ", method=" + method + ", createTime=" + createTime + ", host=" + host + ", port=" + port + ", countDownLatch=" + countDownLatch + ", response=" + response + ", sendOk=" + sendOk + '}';
    }

}
