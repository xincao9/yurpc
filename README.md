## jsonrpc

### High performance rpc frame


> **When using, you only need to add your own log framework that implements slf4j-api. It is strongly recommended to use logback.**

### example

<pre>
Place the configuration file under classpath config.properties

jsonrpc.client.serverList=localhost:12306
jsonrpc.client.connectionTimeoutMS=5000
jsonrpc.client.invokeTimeoutMS=1000

jsonrpc.server.port=12306
jsonrpc.server.ioThreadBoss=1
jsonrpc.server.ioThreadWorker=4

</pre>

<pre>
public class JsonRPCServerTest {

    private static JsonRPCServer jsonRPCServer;

    @BeforeClass
    public static void setUpClass() throws Throwable {
        jsonRPCServer = JsonRPCServer.defaultJsonRPCServer();
        jsonRPCServer.register(new SayServiceImpl());
        jsonRPCServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws Throwable {
        jsonRPCServer.shutdown();
    }

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
    public void testMethod() throws Throwable {
        JsonRPCClient jsonRPCClient = JsonRPCClient.defaultJsonRPCClient();
        jsonRPCClient.start();
        SayService sayService = jsonRPCClient.proxy(SayService.class);
        for (int no = 0; no < 100; no++) {
            String value = RandomStringUtils.randomAscii(128);
            Say say = new Say(no, value);
            System.out.println(sayService.perform(Collections.singletonMap(no, say)));
        }
        jsonRPCClient.shutdown();
    }

    public static class SayServiceImpl implements SayService {

        @Override
        public Map<Integer, Say> perform(Map<Integer, Say> saies) {
            return saies;
        }

    }
}
</pre>

https://issues.sonatype.org/browse/OSSRH-47112