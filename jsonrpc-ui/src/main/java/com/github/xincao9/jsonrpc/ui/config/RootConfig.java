package com.github.xincao9.jsonrpc.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

/**
 * 根配置
 * 
 * @author xincao9@gmail.com
 */
@Configuration
public class RootConfig {

    /**
     * 命名参数jdbc模板
     * 
     * @param jdbcTemplate jdbc模板
     * @return 命名参数jdbc模板
     */
    @Bean
    public NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport(JdbcTemplate jdbcTemplate) {
        NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport = new NamedParameterJdbcDaoSupport();
        namedParameterJdbcDaoSupport.setJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcDaoSupport;
    }
}
