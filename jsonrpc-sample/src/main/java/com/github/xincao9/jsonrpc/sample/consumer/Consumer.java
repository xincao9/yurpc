package com.github.xincao9.jsonrpc.sample.consumer;

import com.github.xincao9.jsonrpc.core.client.JsonRPCClient;
import com.github.xincao9.jsonrpc.sample.Say;
import com.github.xincao9.jsonrpc.sample.SayService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xincao9@gmail.com
 */
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    public static void main (String... args) throws Throwable {
        JsonRPCClient jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
        jsonRPCClient.start();
        SayService sayService = jsonRPCClient.proxy(SayService.class);
        for (int no = 0; no < 100; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Say say = new Say(no, value);
            System.out.println(sayService.perform(say));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                jsonRPCClient.shutdown();
            } catch (Throwable ex) {
                LOGGER.error(ex.getMessage());
            }
        }));
    }
}