package com.github.xincao9.jsonrpc.benchmark.provider.dubbo;

import com.github.xincao9.jsonrpc.benchmark.StreamService;
import org.apache.dubbo.config.annotation.Service;

/**
 * 流服务
 *
 * @author xincao9@gmail.com
 */
@Service(version = "1.0.0")
public class StreamServiceImpl implements StreamService {

    /**
     * 执行
     *
     * @param str 字符串
     * @return 字符串
     */
    @Override
    public String perform(String str) {
        return str;
    }

}
