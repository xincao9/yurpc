package com.github.xincao9.jsonrpc.ui.repository;

import com.github.xincao9.jsonrpc.core.constant.MetricConsts;
import com.github.xincao9.jsonrpc.ui.entity.Timer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * 定时器仓库
 * 
 * @author xincao9@gmail.com
 */
@Repository
public class TimerDAO {

    private static final String TIMER_BY_METHOD = "select method, ct, sum(one_minute_rate) as one_minute_rate, sum(five_minute_rate) as five_minute_rate, sum(fifteen_minute_rate) as fifteen_minute_rate from timer where method=:method group by ct order by ct desc limit 100";
    private static final String METHODS = "select method from timer group by method";
    private static final String SAVE = "insert into timer (host, port, method, count, one_minute_rate, five_minute_rate, fifteen_minute_rate, ct) values (:host,:port,:method,:count,:one_minute_rate,:five_minute_rate,:fifteen_minute_rate,:ct)";

    @Autowired
    private NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport;

    /**
     * 获得定时器列表
     * 
     * @param method 方法
     * @return 定时器列表
     */
    public List<Timer> getTimerByMethod(String method) {
        Map<String, Object> map = new HashMap();
        map.put("method", method);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(map);
        List<Timer> timers =  namedParameterJdbcDaoSupport.getNamedParameterJdbcTemplate().query(TIMER_BY_METHOD, sqlParameterSource, (rs, rowNum) -> {
            Timer timer = new Timer();
            timer.setMethod(rs.getString(MetricConsts.METHOD));
            timer.setCt(rs.getString("ct"));
            timer.setOneMinuteRate(rs.getLong(MetricConsts.ONE_MINUTE_RATE));
            timer.setFiveMinuteRate(rs.getLong(MetricConsts.FIVE_MINUTE_RATE));
            timer.setFifteenMinuteRate(rs.getLong(MetricConsts.FIFTEEN_MINUTE_RATE));
            return timer;
        });
        Collections.reverse(timers);
        return timers;
    }

    public List<String> getMethods () {
        return namedParameterJdbcDaoSupport.getNamedParameterJdbcTemplate().query(METHODS, (rs, rowNum) -> {
            return rs.getString("method");
        });
    }

    /**
     * 保存
     * 
     * @param timer 参数
     */
    public void save (Map<String, Object> timer) {
        namedParameterJdbcDaoSupport.getNamedParameterJdbcTemplate().update(SAVE, timer);
    }
}
