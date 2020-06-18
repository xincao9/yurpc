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
