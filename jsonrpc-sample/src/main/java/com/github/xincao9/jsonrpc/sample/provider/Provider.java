package com.github.xincao9.jsonrpc.sample.provider;

import com.github.xincao9.jsonrpc.core.server.JsonRPCServer;
import com.github.xincao9.jsonrpc.sample.SayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xincao9@gmail.com
 */
public class Provider {

    private static final Logger LOGGER = LoggerFactory.getLogger(Provider.class);

    public static void main(String... args) throws Throwable {
        JsonRPCServer jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
        jsonRPCServer.register(new SayServiceImpl());
        jsonRPCServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                jsonRPCServer.shutdown();
            } catch (Throwable ex) {
                LOGGER.error(ex.getMessage());
            }
        }));
    }
}
