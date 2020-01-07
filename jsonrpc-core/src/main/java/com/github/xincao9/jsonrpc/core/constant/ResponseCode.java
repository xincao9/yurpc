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
package com.github.xincao9.jsonrpc.core.constant;

/**
 * 响应码
 * 
 * @author xincao9@gmail.com
 */
public class ResponseCode {

    public static final Integer OK = 0;
    public static final String OK_MSG = "OK";
    public static final Integer INVOKE_TIMEOUT = 1;
    public static final String INVOKE_TIMEOUT_MSG = "INVOKE TIMEOUT";
    public static final Integer CONNECTION_FAILURE = 2;
    public static final String CONNECTION_FAILURE_MSG = "CONNECTION FAILURE";
    public static final Integer SERVER_ERROR = 3;
    public static final String SERVER_ERROR_MSG = "SERVER ERROR";
    public static final Integer PARAMETER_ERROR = 4;
    public static final String PARAMETER_ERROR_MSG = "PARAMETER ERROR";
    public static final Integer NOT_FOUND_COMPONENT = 5;
    public static final String NOT_FOUND_COMPONENT_MSG = "NOT FOUND COMPONENT";
    public static final Integer NOT_FOUND_METHOD = 6;
    public static final String NOT_FOUND_METHOD_MSG = "NOT FOUND METHOD";
    
}
