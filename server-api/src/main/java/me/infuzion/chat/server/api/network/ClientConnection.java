/*
 * Copyright 2018 Srikavin Ramkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.infuzion.chat.server.api.network;

import infuzion.chat.common.DataType;
import infuzion.chat.common.network.packet.NetworkPacket;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ClientConnection {
    void close() throws IOException;

    int available() throws IOException;

    void write(byte[] bytes) throws IOException;

    ByteBuffer read(int amount) throws IOException;

    /**
     * Reads an entire packet from the underlying connection
     * The first two bytes must be a short containing the packet signature
     * The rest of the bytes will be the packet frame
     * The ByteBuffer does not include the {@link DataType#EndOfData} byte
     *
     * @return A ByteBuffer containing an entire packet
     *
     * @throws IOException
     */
    ByteBuffer readMessage() throws IOException;

    @Deprecated
    void sendMessage(DataType header, byte[] message) throws IOException;

    @Deprecated
    void sendMessage(DataType header, String message) throws IOException;

    void sendPacket(NetworkPacket packet) throws IOException;
}
