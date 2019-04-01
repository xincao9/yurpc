package com.github.xincao9.jsonrpc.benchmark.provider.jsonrpc;

import com.github.xincao9.jsonrpc.benchmark.StreamService;
import com.github.xincao9.jsonrpc.spring.boot.starter.JsonRPCService;

/**
 * 流服务
 *
 * @author xincao9@gmail.com
 */
@JsonRPCService
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
