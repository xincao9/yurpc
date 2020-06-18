package com.github.xincao9.yurpc.sample.consumer;

import com.github.xincao9.ptk.core.PTKCore;
import com.github.xincao9.ptk.core.source.SequenceSource;

public class Benchmark {

    public static void main(String... args) throws Throwable {
        PTKCore.bootstrap(new SequenceSource(1000000), args);
    }
}
