package com.github.xincao9.jsonrpc.sample.provider;

import com.github.xincao9.jsonrpc.core.server.JsonRPCServer;
import com.github.xincao9.jsonrpc.sample.SayService;
import com.github.xincao9.jsonrpc.sample.SayServiceImpl;
import com.github.xincao9.jsonrpc.spring.boot.starter.EnableJsonRPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableJsonRPC(server = true)
public class ApplicationProvider {

    @Autowired
    private JsonRPCServer jsonRPCServer;

    @Bean
    public SayService sayService () {
        SayService sayService = new SayServiceImpl();
        jsonRPCServer.register(sayService);
        return sayService;
    }

    public static void main(String... args) {
        SpringApplication.run(ApplicationProvider.class, args);
    }
}
