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
package com.github.xincao9.jsonrpc.server;

import com.github.xincao9.jsonrpc.common.StringDecoder;
import com.github.xincao9.jsonrpc.common.StringEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xincao9@gmail.com
 */
public class JsonRPCServerImpl implements JsonRPCServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonRPCServerImpl.class);

    private final Integer port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private final Integer boss;
    private final Integer worker;

    /**
     *
     */
    public JsonRPCServerImpl() {
        this.port = ServerConfig.port;
        this.boss = ServerConfig.ioThreadBoss;
        this.worker = ServerConfig.ioThreadWorker;
    }

    /**
     *
     * @throws Throwable
     */
    @Override
    public void start() throws Throwable {
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(this.worker) : new NioEventLoopGroup(this.worker);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.setJsonRPCServer(this);
        if (this.boss == 0) {
            bootstrap.group(this.workerGroup);
        } else {
            this.bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(this.boss) : new NioEventLoopGroup(this.boss);
            bootstrap.group(this.bossGroup, this.workerGroup);
        }
        bootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new StringEncoder(),
                                new StringDecoder(),
                                serverHandler,
                                new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS),
                                new ServerHeartbeatHandler()
                        );
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        ChannelFuture f = bootstrap.bind("0.0.0.0", port).addListener((Future<? super Void> future) -> {
            LOGGER.warn("start the jsonrpc service port = {}, cause = {}", this.port, future.cause());
        });
        f.channel().closeFuture().addListener((Future<? super Void> future) -> {
            LOGGER.warn("turn off jsonrpc service port = {}, cause = {}", this.port, future.cause());
        });
    }

    /**
     *
     * @throws Throwable
     */
    @Override
    public void shutdown() throws Throwable {
        if (this.workerGroup != null) {
            this.workerGroup.shutdownGracefully();
        }
        if (this.bossGroup != null) {
            this.bossGroup.shutdownGracefully();
        }
    }

    private final Map<String, Object> componentes = new HashMap();

    /**
     * 
     * @param <T>
     * @param obj 
     */
    @Override
    public <T> void register (T obj) {
        Objects.requireNonNull(obj);
        Class<?>[] clazzes = obj.getClass().getInterfaces();
        if (clazzes == null || clazzes.length <= 0) {
            LOGGER.error("class = {} invalid format", obj.getClass().getCanonicalName());
            return;
        }
        for (Class clazz : clazzes) {
            componentes.put(clazz.getTypeName(), obj);
        }
    }

    /**
     * 
     * @param name
     * @return 
     */
    @Override
    public Object getBean (String name) {
        return componentes.get(name);
    }
}
