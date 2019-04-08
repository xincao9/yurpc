/*
 * Copyright 2019 xincao9@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
