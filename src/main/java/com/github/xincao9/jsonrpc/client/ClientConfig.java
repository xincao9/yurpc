/*
 * Copyright 2018 xincao9@gmail.com.
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
package com.github.xincao9.jsonrpc.client;

import com.github.xincao9.jsonrpc.common.Pair;
import com.github.xincao9.jsonrpc.constant.ClientConfigConsts;
import com.github.xincao9.jsonrpc.util.PropertiesUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端配置
 * 
 * @author xincao9@gmail.com
 */
public class ClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConfig.class);

    public static List<Pair<String, Integer>> serverList = new ArrayList(1);
    public static Integer connectionTimeoutMS;
    public static Integer invokeTimeoutMS;

    /**
     * 初始化客户端配置
     * 
     * @param filename 配置文件名
     * @return 初始化结果
     */
    public static Boolean init (String filename) {
        try {
            Properties pros = PropertiesUtils.read(filename, ClientConfigConsts.DEFAULT_CONFIG_FILENAME);
            String serverListStr = pros.getProperty(ClientConfigConsts.SERVER_LIST, ClientConfigConsts.DEFAULT_SERVER_LIST);
            String[] servers = serverListStr.split(",");
            for (String server : servers) {
                serverList.add(new Pair(server.split(":")[0], Integer.valueOf(server.split(":")[1])));
            }
            connectionTimeoutMS = Integer.valueOf(pros.getProperty(ClientConfigConsts.CONNECTION_TIMEOUT_MS, ClientConfigConsts.DEFAULT_CONNECTION_TIMEOUT_MS));
            invokeTimeoutMS = Integer.valueOf(pros.getProperty(ClientConfigConsts.INVOKE_TIMEOUT_MS, ClientConfigConsts.DEFAULT_INVOKE_TIMEOUT_MS));
            return true;
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage());
        }
        return false;
    }
}
