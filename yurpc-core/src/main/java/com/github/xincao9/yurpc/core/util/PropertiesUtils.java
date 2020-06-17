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
package com.github.xincao9.yurpc.core.util;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性工具类
 *
 * @author xincao9@gmail.com
 */
public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 读取属性文件
     *
     * @param filename
     * @param defaultFilename
     * @return
     */
    public static Properties read(String filename, String defaultFilename) {
        if (StringUtils.isBlank(filename)) {
            LOGGER.warn("filename is empty");
            filename = defaultFilename;
        }
        try {
            Properties pros = new Properties();
            pros.load(PropertiesUtils.class.getResourceAsStream(filename));
            return pros;
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage());
        }
        return new Properties();
    }

}
