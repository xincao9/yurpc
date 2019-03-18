# jsonrpc

### simple rpc scheme

> 使用时，仅需要添加自己实现了slf4j-api的log框架，强烈推荐使用logback

<pre>
/**
 *
 * @author xincao9@gmail.com
 */
public class JsonRPCServerTest {

    public static class Say {

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

    public static interface SayService {

        Map<Integer, Say> perform(Map<Integer, Say> saies);
    }

    @Test
    public void testPingMethod() throws Throwable {
        JsonRPCServer jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
        jsonRPCServer.register(new SayServiceImpl());
        jsonRPCServer.start();
        JsonRPCClient jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
        jsonRPCClient.start();
        SayService sayService = jsonRPCClient.proxy(SayService.class);
        for (int no = 0; no < 100; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Say say = new Say(no, value);
            System.out.println(sayService.perform(Collections.singletonMap(no, say)));
        }
        jsonRPCClient.shutdown();
        jsonRPCServer.shutdown();
    }

    public static class SayServiceImpl implements SayService {

        @Override
        public Map<Integer, Say> perform(Map<Integer, Say> saies) {
            return saies;
        }

    }
}
</pre>