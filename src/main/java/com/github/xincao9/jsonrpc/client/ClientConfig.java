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
 *
 * @author xincao9@gmail.com
 */
public class ClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConfig.class);

    public static List<Pair<String, Integer>> serverList = new ArrayList(1);
    public static Integer connectionTimeoutMS;
    public static Integer invokeTimeoutMS;

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
