## yurpc

### 高性能RPC框架

#### yurpc 基于java的高性能开源RPC框架，使用方式上类似dubbo，提倡面向接口编程，如果您熟悉dubbo很容易迁移到yurpc;并享受高性能带来的服务体验。提供springboot高度集成starter包，实现零配置使用

![logo](https://github.com/xincao9/yurpc/blob/master/architecture.png)

#### yurpc 实战

**_maven 依赖_**

```
<dependency>
    <groupId>com.github.xincao9</groupId>
    <artifactId>yurpc-spring-boot-starter</artifactId>
    <version>1.2.5</version>
</dependency>
```

**_实体定义_**

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

**_服务接口定义_**

```
public interface SayService {

    Say perform(Say say);
}
```

**_提供者实现服务接口_**

```
@YuRPCService
public class SayServiceImpl implements SayService {

    @Override
    public Say perform(Say say) {
        return say;
    }

}
```

**_服务提供者启动类_**

```
@SpringBootApplication
@EnableYuRPC(server = true)
public class ApplicationProvider {

    public static void main(String... args) {
        SpringApplication.run(ApplicationProvider.class, args);
    }
}
```

**_服务消费者启动类_**

```
@SpringBootApplication
@EnableYuRPC(client = true)
public class ApplicationConsumer {

    @YuRPCAutowired
    private SayService sayService; // 可以在任何spring bean 中注入yurpc服务


    public static void main(String... args) {
        SpringApplication.run(ApplicationConsumer.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            for (int no = 0; no < 100; no++) {
                String value = RandomStringUtils.randomAscii(128);
                Say say = new Say(no, value);
                System.out.println(sayService.perform(say)); // 远程调用yurpc服务
            }
        };
    }

}
```
**_application.properties_**

```
## 服务发现注册或订阅的zk地址，暂时只支持zookeeper的注册中心
yurpc.discovery.zookeeper=localhost:2181

## 消费者配置
yurpc.client.connectionTimeoutMS=5000 // 服务连接超时时间
yurpc.client.invokeTimeoutMS=1000 // 服务调用超时时间

## 提供者配置
yurpc.server.port=12306 // 服务监听端口
```

**_温馨提示_**

* 欢迎查看示例 [examples](https://github.com/xincao9/yurpc/tree/master/yurpc-sample)
* yurpc 本身并不是必须和springboot一起使用，在示例中可以查看
* 单独使用的话，配置文件名为 config.properties，在示例中可以查看
* @EnableYuRPC(server = true, client = true) 意味着服务角色同为消费端和提供者使用

#### 联系方式

* [https://github.com/xincao9/yurpc/issues](https://github.com/xincao9/yurpc/issues)
* [https://issues.sonatype.org/browse/OSSRH-47112](https://issues.sonatype.org/browse/OSSRH-47112)
* xincao9@gmail.com
