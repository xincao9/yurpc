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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xincao9.jsonrpc.core.constant.ResponseCode;

/**
 * 响应体
 * 
 * @author xincao9@gmail.com
 * @param <T>
 */
public class Response<T> {

    private Long id; // response id
    private Integer code = ResponseCode.OK; // response code
    private T data;
    private String msg = ResponseCode.OK_MSG;
    private final Long createTime = System.currentTimeMillis();

    public static <T> Response<T> createResponse(Long id, T data) {
        Response response = new Response();
        response.setId(id);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> createResponse(Long id, Integer code, String msg) {
        Response response = new Response();
        response.setId(id);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

}
