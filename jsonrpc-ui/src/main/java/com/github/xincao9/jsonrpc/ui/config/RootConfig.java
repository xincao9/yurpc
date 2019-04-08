package com.github.xincao9.jsonrpc.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

/**
 *
 * @author xincao9@gmail.com
 */
@Configuration
public class RootConfig {

    @Bean
    public NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport(JdbcTemplate jdbcTemplate) {
        NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport = new NamedParameterJdbcDaoSupport();
        namedParameterJdbcDaoSupport.setJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcDaoSupport;
    }
}
