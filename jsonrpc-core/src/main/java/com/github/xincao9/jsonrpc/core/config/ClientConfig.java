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
package com.github.xincao9.jsonrpc.core.config;

import com.github.xincao9.jsonrpc.core.protocol.Pair;
import com.github.xincao9.jsonrpc.core.constant.ClientConsts;
import com.github.xincao9.jsonrpc.core.util.PropertiesUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 客户端配置
 *
 * @author xincao9@gmail.com
 */
public class ClientConfig {

    public static List<Pair<String, Integer>> serverList = new ArrayList(1);
    public static Integer connectionTimeoutMS;
    public static Integer invokeTimeoutMS;

    /**
     * 初始化客户端配置
     *
     * @param filename 配置文件名
     */
    public static void init(String filename) {
        Properties pros = PropertiesUtils.read(filename, ClientConsts.DEFAULT_CONFIG_FILENAME);
        init(pros);
    }

    /**
     * 初始化客户端配置
     *
     * @param pros 属性文件
     */
    public static void init(Properties pros) {
        String serverListStr = pros.getProperty(ClientConsts.SERVER_LIST, ClientConsts.DEFAULT_SERVER_LIST);
        String[] servers = serverListStr.split(",");
        for (String server : servers) {
            serverList.add(new Pair(server.split(":")[0], Integer.valueOf(server.split(":")[1])));
        }
        connectionTimeoutMS = Integer.valueOf(pros.getProperty(ClientConsts.CONNECTION_TIMEOUT_MS, ClientConsts.DEFAULT_CONNECTION_TIMEOUT_MS));
        invokeTimeoutMS = Integer.valueOf(pros.getProperty(ClientConsts.INVOKE_TIMEOUT_MS, ClientConsts.DEFAULT_INVOKE_TIMEOUT_MS));
    }

}
