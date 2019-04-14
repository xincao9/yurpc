java -Dspring.profiles.active=jsonrpc-provider -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.provider.jsonrpc.JsonRPCApplication
java -Dspring.profiles.active=jsonrpc-consumer -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.consumer.jsonrpc.JsonRPCApplication
wrk -c 16 -t 2 -d 30s 'http://localhost:8001/jsonrpc/stream'
wrk -c 16 -t 2 -d 30s 'http://localhost:8001/jsonrpc/sleep'

java -Dspring.profiles.active=dubbo-provider -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.provider.dubbo.DubboApplication
java -Dspring.profiles.active=dubbo-consumer -cp target/jsonrpc-benchmark-1.2.5.jar com.github.xincao9.jsonrpc.benchmark.consumer.dubbo.DubboApplication
wrk -c 16 -t 2 -d 30s 'http://localhost:9001/dubbo/stream'
wrk -c 16 -t 2 -d 30s 'http://localhost:9001/dubbo/sleep'
