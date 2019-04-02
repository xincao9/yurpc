## jsonrpc

![logo](https://github.com/xincao9/jsonrpc/blob/master/logo.png)

### high-performance RPC framework. 

#### jsonrpc is a high-performance, Java based open source RPC framework.

#### example

**_Maven dependency_**

```
<dependency>
    <groupId>com.github.xincao9</groupId>
    <artifactId>jsonrpc-spring-boot-starter</artifactId>
    <version>1.2.4</version>
</dependency>
```

**_object_**

```

public class Say {

    private Integer id;
    private String body;

    public Say(Integer id, String body) {
        this.id = id;
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
    
        this.body = body;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
```

**_interface_**

```
public interface SayService {

    Say perform(Say say);
}
```

**_service_**

```
@JsonRPCService
public class SayServiceImpl implements SayService {

    @Override
    public Say perform(Say say) {
        return say;
    }

}
```

**_service provider_**

```
@SpringBootApplication
@EnableJsonRPC(server = true)
public class ApplicationProvider {

    public static void main(String... args) {
        SpringApplication.run(ApplicationProvider.class, args);
    }
}
```

**_service consumer_**

```
@SpringBootApplication
@EnableJsonRPC(client = true)
public class ApplicationConsumer {

    @JsonRPCAutowired
    private SayService sayService;


    public static void main(String... args) {
        SpringApplication.run(ApplicationConsumer.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            for (int no = 0; no < 100; no++) {
                String value = RandomStringUtils.randomAscii(128);
                Say say = new Say(no, value);
                System.out.println(sayService.perform(say));
            }
        };
    }

}
```
**_application.properties_**

```
## discovery support
jsonrpc.discovery.zookeeper=localhost:2181

## consumer
jsonrpc.client.serverList=localhost:12306
jsonrpc.client.connectionTimeoutMS=5000
jsonrpc.client.invokeTimeoutMS=1000

## provider
jsonrpc.server.port=12306
```

**_benchmark_**

```
Get the pressure measurement component
wget https://oss.sonatype.org/service/local/repositories/releases/content/com/github/xincao9/jsonrpc-benchmark/1.2.4/jsonrpc-benchmark-1.2.4.jar

dubbo 压力测试

java -Dspring.profiles.active=jsonrpc-provider -cp target/jsonrpc-benchmark-1.2.4.jar com.github.xincao9.jsonrpc.benchmark.provider.jsonrpc.JsonRPCApplication
java -Dspring.profiles.active=jsonrpc-consumer -cp target/jsonrpc-benchmark-1.2.4.jar com.github.xincao9.jsonrpc.benchmark.consumer.jsonrpc.JsonRPCApplication

wrk -c 16 -t 2 -d 30s 'http://localhost:9001/dubbo/stream'

Running 30s test @ http://localhost:9001/dubbo/stream
  2 threads and 16 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.30ms   12.93ms 294.76ms   97.36%
    Req/Sec     3.04k     1.32k    6.27k    70.67%
  181921 requests in 30.06s, 42.19MB read
Requests/sec:   6052.90
Transfer/sec:      1.40MB

wrk -c 16 -t 2 -d 30s 'http://localhost:9001/dubbo/stream'

Running 30s test @ http://localhost:9001/dubbo/stream
  2 threads and 16 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.69ms    1.88ms  54.01ms   97.53%
    Req/Sec     5.19k   810.88     6.61k    74.67%
  310323 requests in 30.03s, 71.97MB read
Requests/sec:  10332.60
Transfer/sec:      2.40MB

jsonrpc 压力测试

java -Dspring.profiles.active=dubbo-provider -cp target/jsonrpc-benchmark-1.2.4.jar com.github.xincao9.jsonrpc.benchmark.provider.dubbo.DubboApplication
java -Dspring.profiles.active=dubbo-consumer -cp target/jsonrpc-benchmark-1.2.4.jar com.github.xincao9.jsonrpc.benchmark.consumer.dubbo.DubboApplication

wrk -c 16 -t 2 -d 30s 'http://localhost:8001/jsonrpc/stream'

Running 30s test @ http://localhost:8001/jsonrpc/stream
  2 threads and 16 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.07ms    2.39ms  45.15ms   93.64%
    Req/Sec     4.58k     2.12k    8.02k    58.33%
  273896 requests in 30.03s, 63.52MB read
Requests/sec:   9119.72
Transfer/sec:      2.12MB

wrk -c 16 -t 2 -d 30s 'http://localhost:8001/jsonrpc/stream'

Running 30s test @ http://localhost:8001/jsonrpc/stream
  2 threads and 16 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.15ms    1.24ms  46.94ms   98.75%
    Req/Sec     7.13k   460.27     8.19k    80.33%
  426195 requests in 30.01s, 98.84MB read
Requests/sec:  14200.14
Transfer/sec:      3.29MB

结论：jsonrpc 在计算密集型业务中性能优于dubbo 30%,很容易达到网卡吞吐量的极限。分析原因，dubbo中为了适配多协议和耦合多种服务治理模块，导致性能损耗

```

**_tips_**

* Welcome to see detailed examples [examples](https://github.com/xincao9/jsonrpc/tree/master/jsonrpc-sample)
* Not only supports the boot mode of springboot
* Native boot mode, the default configuration file is named config.properties
* @EnableJsonRPC(server = true, client = true) Indicates that the service is a consumer even if the provider

#### Contact

* [issues](https://github.com/xincao9/jsonrpc/issues)
* [sonatype](https://issues.sonatype.org/browse/OSSRH-47112)
* xincao9@gmail.com
