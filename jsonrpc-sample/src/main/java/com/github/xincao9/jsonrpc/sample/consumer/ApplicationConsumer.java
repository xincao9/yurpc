package com.github.xincao9.jsonrpc.sample.consumer;

import com.github.xincao9.jsonrpc.core.client.JsonRPCClient;
import com.github.xincao9.jsonrpc.sample.Say;
import com.github.xincao9.jsonrpc.sample.SayService;
import com.github.xincao9.jsonrpc.spring.boot.starter.EnableJsonRPC;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableJsonRPC(client = true)
public class ApplicationConsumer {

    @Autowired
    private JsonRPCClient jsonRPCClient;
    @Autowired
    private SayService sayService;

    @Bean
    public SayService sayService() {
        return jsonRPCClient.proxy(SayService.class);
    }

    public static void main(String... args) {
        SpringApplication.run(ApplicationConsumer.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                for (int no = 0; no < 100; no++) {
                    String value = RandomStringUtils.randomAscii(128);
                    Say say = new Say(no, value);
                    System.out.println(sayService.perform(say));
                }
            }
        };
    }

}
