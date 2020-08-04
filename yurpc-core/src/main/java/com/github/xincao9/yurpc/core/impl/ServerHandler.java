/*
 * Copyright 2020 xincao9@gmail.com.
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

import com.github.xincao9.yurpc.core.YuRPCServer;
import com.alibaba.fastjson.JSONObject;
import com.github.xincao9.yurpc.core.protocol.Request;
import com.github.xincao9.yurpc.core.protocol.Response;
import com.github.xincao9.yurpc.core.constant.ResponseCode;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private YuRPCServer yuRPCServer;
    private final ExecutorService processor = Executors.newCachedThreadPool();
    private final Map<String, Class> nameClass = new ConcurrentHashMap();

    private void submit(Boolean requestType, Method method, Long rid, Object component, Object[] params, ChannelHandlerContext ctx) {
        processor.submit(() -> {
            try {
                Response response;
                if (requestType) {
                    response = Response.createResponse(rid, method.invoke(component, params));
                } else {
                    method.invoke(component, params);
                    response = Response.createResponse(rid, null);
                }
                ctx.channel().writeAndFlush(response.toString());
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
                exception(ctx, rid, ResponseCode.SERVER_ERROR, ResponseCode.SERVER_ERROR_MSG);
            }
        });
    }

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
            long rid = request.getId();
            try {
                String name = request.getMethod();
                if (StringUtils.isBlank(name)) {
                    exception(ctx, rid, ResponseCode.PARAMETER_ERROR, ResponseCode.PARAMETER_ERROR_MSG);
                    return;
                }
                String classname = StringUtils.substringBeforeLast(name, ".");
                String methodname = StringUtils.substringAfterLast(name, ".");
                Object component = yuRPCServer.getBean(classname);
                if (component == null) {
                    exception(ctx, rid, ResponseCode.NOT_FOUND_COMPONENT, ResponseCode.NOT_FOUND_COMPONENT_MSG);
                    return;
                }
                String[] paramTypes = request.getParamTypes();
                Class<?> clazz = getClass(classname);
                Method method;
                Object[] params = null;
                if (paramTypes == null || paramTypes.length <= 0) {
                    method = clazz.getMethod(methodname);
                } else {
                    Class<?>[] clazzes = new Class<?>[paramTypes.length];
                    params = new Object[paramTypes.length];
                    for (int i = 0; i < paramTypes.length; i++) {
                        clazzes[i] = getClass(paramTypes[i]);
                        if (request.getParams() != null && request.getParams()[i] != null) {
                            params[i] = JSONObject.parseObject(JSONObject.toJSONString(request.getParams()[i]), clazzes[i]);
                        }
                    }
                    method = clazz.getMethod(methodname, clazzes);
                }
                if (method == null) {
                    exception(ctx, rid, ResponseCode.NOT_FOUND_METHOD, ResponseCode.NOT_FOUND_METHOD_MSG);
                    return;
                }
                submit(request.getRequestType(), method, rid, component, params, ctx);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
                exception(ctx, rid, ResponseCode.SERVER_ERROR, ResponseCode.SERVER_ERROR_MSG);
            }
        } else {
        }
    }

    /**
     *
     * @param classname
     * @return
     * @throws ClassNotFoundException
     */
    private Class getClass(String classname) throws ClassNotFoundException {
        if (!nameClass.containsKey(classname)) {
            nameClass.put(classname, Class.forName(classname));
        }
        return nameClass.get(classname);
    }

    /**
     *
     * @param ctx
     * @param id
     * @param responseCode
     * @param msg
     */
    private void exception(ChannelHandlerContext ctx, Long id, Integer responseCode, String msg) {
        ctx.channel().writeAndFlush(Response.createResponse(id, responseCode, msg).toString());
    }

    /**
     * 修改器
     *
     * @param yuRPCServer 服务组件
     */
    public void setYuRPCServer(YuRPCServer yuRPCServer) {
        this.yuRPCServer = yuRPCServer;
    }
}
