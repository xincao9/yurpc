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
package com.github.xincao9.jsonrpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息解码
 *
 * @author xincao9@gmail.com
 */
public class StringDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringDecoder.class);

    /**
     *
     * @param chc
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf byteBuf, List<Object> list) throws Exception {
        try {
            do {
                int readerIndex = byteBuf.readerIndex();
                int readableBytesSize = byteBuf.readableBytes();
                if (readableBytesSize < 4) {
                    byteBuf.readerIndex(readerIndex);
                    break;
                }
                int size = byteBuf.readInt();
                if (readableBytesSize - 4 < size) {
                    byteBuf.readerIndex(readerIndex);
                    break;
                }
                byteBuf.readerIndex(readerIndex);
                ByteBuffer byteBuffer = ByteBuffer.allocate(4 + size);
                byteBuf.readBytes(byteBuffer);
                byte[] data = byteBuffer.array();
                LOGGER.debug("StringDecoder.decode() hex = {}", Hex.encodeHex(data));
                byte[] body = Arrays.copyOfRange(data, 4, data.length);
                list.add(new String(body, "UTF-8"));
            } while (byteBuf.isReadable());
        } finally {
            if (byteBuf.isReadable()) {
                byteBuf.discardReadBytes();
            }
        }
    }

}
