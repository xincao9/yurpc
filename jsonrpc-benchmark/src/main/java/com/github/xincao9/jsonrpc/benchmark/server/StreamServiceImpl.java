package com.github.xincao9.jsonrpc.benchmark.server;

import com.github.xincao9.jsonrpc.benchmark.StreamService;

/**
 * 流服务
 * 
 * @author xincao9@gmail.com
 */
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
