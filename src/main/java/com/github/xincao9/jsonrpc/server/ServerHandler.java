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
package com.github.xincao9.jsonrpc.server;

import com.alibaba.fastjson.JSONObject;
import com.github.xincao9.jsonrpc.common.Request;
import com.github.xincao9.jsonrpc.common.Response;
import com.github.xincao9.jsonrpc.constant.ResponseCode;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务输入流处理
 * 
 * @author xincao9@gmail.com
 */
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    private JsonRPCServer jsonRPCServer;

    /**
     * 处理请求
     * 
     * @param ctx 上下文
     * @param str 消息体
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        Request request = JSONObject.parseObject(str, Request.class);
        if (request.getEventType() == false) {
            Response response;
            try {
                String name = request.getMethod();
                if (StringUtils.isBlank(name)) {
                    response = Response.createResponse(request.getId(), ResponseCode.PARAMETER_ERROR, ResponseCode.PARAMETER_ERROR_MSG);
                    ctx.channel().writeAndFlush(response.toString());
                    return;
                }
                String classname = StringUtils.substringBeforeLast(name, ".");
                String methodname = StringUtils.substringAfterLast(name, ".");
                Object component = jsonRPCServer.getBean(classname);
                if (component == null) {
                    response = Response.createResponse(request.getId(), ResponseCode.NOT_FOUND_COMPONENT, ResponseCode.NOT_FOUND_COMPONENT_MSG);
                    ctx.channel().writeAndFlush(response.toString());
                    return;
                }
                String[] paramTypes = request.getParamTypes();
                Class<?> clazz = Class.forName(classname);
                Method method;
                Object[] params = null;
                if (paramTypes == null || paramTypes.length <= 0) {
                    method = clazz.getMethod(methodname);
                } else {
                    Class<?>[] clazzes = new Class<?>[paramTypes.length];
                    params = new Object[paramTypes.length];
                    for (int i = 0; i < paramTypes.length; i++) {
                        clazzes[i] = Class.forName(paramTypes[i]);
                        if (request.getParams() != null && request.getParams()[i] != null) {
                            params[i] = JSONObject.parseObject(JSONObject.toJSONString(request.getParams()[i]), clazzes[i]);
                        }
                    }
                    method = clazz.getMethod(methodname, clazzes);
                }
                if (method == null) {
                    response = Response.createResponse(request.getId(), ResponseCode.NOT_FOUND_METHOD, ResponseCode.NOT_FOUND_METHOD_MSG);
                    ctx.channel().writeAndFlush(response.toString());
                    return;
                }
                if (request.getRequestType()) {
                    response = Response.createResponse(request.getId(), method.invoke(component, params));
                } else {
                    method.invoke(component, request.getParams());
                    response = Response.createResponse(request.getId(), null);
                }
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
                response = Response.createResponse(request.getId(), ResponseCode.SERVER_ERROR, ResponseCode.SERVER_ERROR_MSG);
            }
            ctx.channel().writeAndFlush(response.toString());
        } else {
        }
    }

    /**
     * 修改器
     * 
     * @param jsonRPCServer 服务组件
     */
    public void setJsonRPCServer(JsonRPCServer jsonRPCServer) {
        this.jsonRPCServer = jsonRPCServer;
    }
}
