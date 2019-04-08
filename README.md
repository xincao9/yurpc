## jsonrpc

![logo](https://github.com/xincao9/jsonrpc/blob/master/logo.png)

### high-performance RPC framework. 

#### jsonrpc is a high-performance, Java based open source RPC framework.

#### JSONRPC IN ACTION

**_Maven dependency_**

```
<dependency>
    <groupId>com.github.xincao9</groupId>
    <artifactId>jsonrpc-spring-boot-starter</artifactId>
    <version>1.2.5</version>
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
wget https://oss.sonatype.org/service/local/repositories/releases/content/com/github/xincao9/jsonrpc-benchmark/1.2.5/jsonrpc-benchmark-1.2.5.jar

dubbo pressure test
java -Dspring.profiles.active=dubbo-provider -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.provider.dubbo.DubboApplication
java -Dspring.profiles.active=dubbo-consumer -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.consumer.dubbo.DubboApplication
wrk -c 16 -t 2 -d 30s 'http://localhost:9001/dubbo/stream'
wrk -c 128 -t 16 -d 30s 'http://localhost:9001/dubbo/sleep'

jsonrpc pressure test
java -Dspring.profiles.active=jsonrpc-provider -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.provider.jsonrpc.JsonRPCApplication
java -Dspring.profiles.active=jsonrpc-consumer -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.consumer.jsonrpc.JsonRPCApplication
wrk -c 16 -t 2 -d 30s 'http://localhost:8001/jsonrpc/stream'
wrk -c 128 -t 16 -d 30s 'http://localhost:8001/jsonrpc/sleep'
```

**Data Form**

<table>
<tr>
<td>Frame</td>
<td>interface</td>
<td>thread</td>
<td>link</td>
<td>Numbers</td>
<td>QPS</td>
<td>throughput</td>
<td>Average time </td>
</tr>
<tr>
<td>dubbo</td>
<td>/dubbo/stream</td>
<td>2</td>
<td>16</td>
<td>1</td>
<td>6052.90</td>
<td>42.19MB</td>
<td>4.30ms</td>
</tr>
<tr>
<td>dubbo</td>
<td>/dubbo/stream</td>
<td>2</td>
<td>16</td>
<td>2</td>
<td>10332.60</td>
<td>71.97MB</td>
<td>1.69ms</td>
</tr>
<tr>
<td>jsonrpc</td>
<td>/jsonrpc/stream</td>
<td>2</td>
<td>16</td>
<td>1</td>
<td>9119.72</td>
<td>63.52MB</td>
<td>2.07ms</td>
</tr>
<tr>
<td>jsonrpc</td>
<td>/jsonrpc/stream</td>
<td>2</td>
<td>16</td>
<td>2</td>
<td>14200.14</td>
<td>98.84MB</td>
<td>1.15ms</td>
</tr>
<tr>
<td>dubbo</td>
<td>/dubbo/sleep</td>
<td>16</td>
<td>128</td>
<td>1</td>
<td>4477.63</td>
<td>17.99MB</td>
<td>28.59ms</td>
</tr>
<tr>
<td>dubbo</td>
<td>/dubbo/sleep</td>
<td>16</td>
<td>128</td>
<td>2</td>
<td>4378.68</td>
<td>17.59MB</td>
<td>29.20ms</td>
</tr>
<tr>
<td>jsonrpc</td>
<td>/jsonrpc/sleep</td>
<td>16</td>
<td>128</td>
<td>1</td>
<td>4397.55</td>
<td>17.67MB</td>
<td>29.60ms</td>
</tr>
<tr>
<td>jsonrpc</td>
<td>/jsonrpc/sleep</td>
<td>16</td>
<td>128</td>
<td>2</td>
<td>4514.74</td>
<td>18.14MB</td>
<td>28.31ms</td>
</tr>
</table>

**Conclusion**: *jsonrpc outperforms dubbo 30% in compute-intensive services and easily reaches the limit of NIC throughput. Analysis of the reasons, dubbo in order to adapt to multiple protocols and coupling multiple service governance modules, resulting in performance loss. There is no gap in IO-intensive*

**_tips_**

* Welcome to see detailed examples [examples](https://github.com/xincao9/jsonrpc/tree/master/jsonrpc-sample)
* Not only supports the boot mode of springboot
* Native boot mode, the default configuration file is named config.properties
* @EnableJsonRPC(server = true, client = true) Indicates that the service is a consumer even if the provider

### Install jsonrpc-ui

```
## create a table of storage jsonrpc-ui information

CREATE DATABASE `jsonrpc-ui` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jsonrpc-ui`;

CREATE TABLE `timer` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `host` varchar(64) NOT NULL,
  `port` int(11) NOT NULL,
  `method` varchar(128) NOT NULL,
  `count` bigint(11) unsigned NOT NULL,
  `one_minute_rate` bigint(11) unsigned NOT NULL,
  `five_minute_rate` bigint(11) unsigned NOT NULL,
  `fifteen_minute_rate` bigint(11) unsigned NOT NULL,
  `ct` varchar(64) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

**_Install_**

1. [download jsonrpc-ui](https://search.maven.org/remotecontent?filepath=com/github/xincao9/jsonrpc-ui/1.2.5/jsonrpc-ui-1.2.5.jar)
2. java -jar jsonrpc-ui-1.2.5.jar --jsonrpc.discovery.zookeeper=localhost:2181 --spring.datasource.url=jdbc:mysql://localhost:3306/jsonrpc-ui?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true --spring.datasource.username=root --spring.datasource.password=
3. [ui url](http://localhost:9090)

**_Used_**

![ui](https://github.com/xincao9/jsonrpc/blob/master/ui.png)

#### Contact

* [issues](https://github.com/xincao9/jsonrpc/issues)
* [sonatype](https://issues.sonatype.org/browse/OSSRH-47112)
* xincao9@gmail.com
