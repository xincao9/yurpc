package com.github.xincao9.jsonrpc.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xincao9@gmail.com
 */
public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 
     * @param filename
     * @param defaultFilename
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static Properties read(String filename, String defaultFilename) throws IOException {
        if (StringUtils.isBlank(filename)) {
            LOGGER.warn("filename is empty");
        }
        filename = defaultFilename;
        Properties pros = new Properties();
        pros.load(PropertiesUtils.class.getResourceAsStream(filename));
        return pros;
    }

}
