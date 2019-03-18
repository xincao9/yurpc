package com.github.xincao9.jsonrpc.server;

import com.github.xincao9.jsonrpc.constant.ConfigConsts;
import com.github.xincao9.jsonrpc.util.PropertiesUtils;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xincao9@gmail.com
 */
public class ServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfig.class);

    public static Integer port;
    public static Integer ioThreadBoss;
    public static Integer ioThreadWorker;

    public static Boolean init(String filename) {
        try {
            Properties pros = PropertiesUtils.read(filename, ConfigConsts.DEFAULT_SERVER_CONFIG_FILENAME);
            port = Integer.valueOf(String.valueOf(pros.getProperty(ConfigConsts.PORT, ConfigConsts.DEFAULT_PORT)));
            ioThreadBoss = Integer.valueOf(String.valueOf(pros.getProperty(ConfigConsts.IO_THREAD_BOSS, ConfigConsts.DEFAULT_IO_THREAD_BOSS)));
            ioThreadWorker = Integer.valueOf(String.valueOf(pros.getProperty(ConfigConsts.IO_THREAD_WORKER, ConfigConsts.DEFAULT_IO_THREAD_WORKER)));
            return true;
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage());
        }
        return false;

    }
}
