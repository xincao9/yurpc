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
package com.github.xincao9.jsonrpc.client;

import com.github.xincao9.jsonrpc.common.Request;
import com.alibaba.fastjson.JSONObject;
import com.github.xincao9.jsonrpc.common.Response;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Map;

/**
 * 客户端输入流处理器
 * 
 * @author xincao9@gmail.com
 */
@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private JsonRPCClient jsonRPCClient;

    /**
     * 读取输入流处理
     * 
     * @param ctx channel上下文
     * @param msg 消息
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Response response = JSONObject.parseObject(msg, Response.class);
        long responseId = response.getId();
        Map<Long, Request> requests = jsonRPCClient.getRequests();
        if (requests.containsKey(responseId)) {
            Request request = requests.get(responseId);
            requests.remove(responseId);
            request.putResponse(response);
        }
    }

    /**
     * 修改器
     * 
     * @param jsonRPCClient 客户端
     */
    public void setJsonRPCClient(JsonRPCClient jsonRPCClient) {
        this.jsonRPCClient = jsonRPCClient;
    }

}