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
import io.netty.util.concurrent.Future;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
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
    private final Map<String, Method> methods = new ConcurrentHashMap();
    private Integer boss = 0;
    private Integer worker = Runtime.getRuntime().availableProcessors();

    /**
     * 
     * @param port
     * @param boss
     * @param worker 
     */
    public JsonRPCServerImpl(Integer port, Integer boss, Integer worker) {
        this.port = port;
        if (boss > 0) {
            this.boss = boss;
        }
        if (worker > 0) {
            this.worker = worker;
        }
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
                                serverHandler);
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

    /**
     * 
     * @param method 
     */
    @Override
    public void register(Method method) {
        Objects.requireNonNull(method);
        this.methods.put(method.getName(), method);
    }

    /**
     * 
     * @param name
     * @return 
     */
    @Override
    public Method getMethod(String name) {
        return this.methods.get(name);
    }
}
