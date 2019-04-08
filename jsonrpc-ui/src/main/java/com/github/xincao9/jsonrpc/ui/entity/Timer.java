package com.github.xincao9.jsonrpc.ui.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时器
 * 
 * @author xincao9@gmail.com
 */
public class Timer implements Serializable {

    private Long id;
    private String host;
    private Integer port;
    private String method;
    private Long count;
    private Long oneMinuteRate;
    private Long fiveMinuteRate;
    private Long fifteenMinuteRate;
    private String ct;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getOneMinuteRate() {
        return oneMinuteRate;
    }

    public void setOneMinuteRate(Long oneMinuteRate) {
        this.oneMinuteRate = oneMinuteRate;
    }

    public Long getFiveMinuteRate() {
        return fiveMinuteRate;
    }

    public void setFiveMinuteRate(Long fiveMinuteRate) {
        this.fiveMinuteRate = fiveMinuteRate;
    }

    public Long getFifteenMinuteRate() {
        return fifteenMinuteRate;
    }

    public void setFifteenMinuteRate(Long fifteenMinuteRate) {
        this.fifteenMinuteRate = fifteenMinuteRate;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
