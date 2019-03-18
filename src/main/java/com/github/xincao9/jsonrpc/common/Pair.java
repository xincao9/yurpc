package com.github.xincao9.jsonrpc.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 *
 * @author xincao9@gmail.com
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {

    private T1 o1;
    private T2 o2;

    public Pair() {
    }

    public Pair(T1 o1, T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public T1 getO1() {
        return o1;
    }

    public void setO1(T1 o1) {
        this.o1 = o1;
    }

    public T2 getO2() {
        return o2;
    }

    public void setO2(T2 o2) {
        this.o2 = o2;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

}
