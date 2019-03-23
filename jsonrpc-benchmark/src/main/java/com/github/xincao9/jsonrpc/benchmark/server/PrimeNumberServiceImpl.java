package com.github.xincao9.jsonrpc.benchmark.server;

import com.github.xincao9.jsonrpc.benchmark.PrimeNumberService;

/**
 * 素数服务
 * 
 * @author xincao9@gmail.com
 */
public class PrimeNumberServiceImpl implements PrimeNumberService {

    /**
     * 执行
     * 
     * @param n 整数
     * @return 是否为素数
     */
    @Override
    public Boolean perform(Integer n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}
