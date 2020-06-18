/*
 * Copyright 2019 xincao9@gmail.com.
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

import com.github.xincao9.ptk.core.Method;
import com.github.xincao9.ptk.core.annotation.Test;
import com.github.xincao9.yurpc.core.YuRPCClient;
import com.github.xincao9.yurpc.sample.Say;
import com.github.xincao9.yurpc.sample.SayService;
import org.apache.commons.lang3.RandomStringUtils;

@Test(name="say")
public class SayMethod extends Method {

    private SayService sayService;

    public SayMethod () {
        YuRPCClient yuRPCClient = YuRPCClient.defaultYuRPCClient();
        try {
            yuRPCClient.start();
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
        sayService = yuRPCClient.proxy(SayService.class);
    }

    @Override
    public void exec(Object params) throws Exception {
        Integer id = (Integer) params;
        String value = RandomStringUtils.randomAscii(128);
        Say say = new Say(id, value);
        sayService.perform(say);
    }
}
