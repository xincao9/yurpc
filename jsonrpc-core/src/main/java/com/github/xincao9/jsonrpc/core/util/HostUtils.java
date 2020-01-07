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
package com.github.xincao9.jsonrpc.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主机工具
 * 
 * @author xincao9@gmail.com
 */
public class HostUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostUtils.class);

    /**
     * 获得本机IP地址
     * 
     * @return 本机IP
     */
    public static String getLocalAddress() {
        try {
            InetAddress candidateAddress = null; // 遍历所有的网络接口
            Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement(); // 在所有的接口下再遍历IP
                Enumeration inetAddrs = iface.getInetAddresses();
                while (inetAddrs.hasMoreElements()) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) { // 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) { // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {  // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost(); // 如果没有发现 non-loopback地址.只能用最次选的方案
            return jdkSuppliedAddress.getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
