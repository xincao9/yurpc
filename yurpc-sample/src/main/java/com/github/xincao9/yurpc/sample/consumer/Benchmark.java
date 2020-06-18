package com.github.xincao9.yurpc.sample.consumer;

import com.github.xincao9.ptk.core.PTKCore;
import com.github.xincao9.ptk.core.source.SequenceSource;

/**
 * java -cp target/yurpc-sample-1.2.5.jar com.github.xincao9.yurpc.sample.consumer.Benchmark -m say -t -1 -c 16
 */
public class Benchmark {

    public static void main(String... args) throws Throwable {
        PTKCore.bootstrap(new SequenceSource(1000000), args);
    }
}
