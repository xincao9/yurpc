/*
 * Copyright 2020 xincao9@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
