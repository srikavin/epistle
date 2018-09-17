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

package me.infuzion.chat.server;

import infuzion.chat.common.DataType;
import infuzion.chat.common.network.packet.ClientHelloPacket;
import infuzion.chat.common.network.packet.MessagePacket;
import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.network.ClientConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ServerClientConnection implements ClientConnection {
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void close() {
        // Don't close System.in
    }

    @Override
    public int available() throws IOException {
        return input.ready() ? 15 : 0;
    }

    @Override
    public ByteBuffer readMessage() throws IOException {
        if (available() == 0) {
            return ByteBuffer.allocate(0);
        }
        String line = input.readLine();
        System.out.println(line);
        ByteBuffer buffer = ByteBuffer.allocate(line.length() + 7);
        buffer.putShort(DataType.Command.value);
        buffer.putInt(line.length());
        buffer.put(line.getBytes(StandardCharsets.UTF_8));
        buffer.put(DataType.EndOfData.byteValue);
        buffer.rewind();
        return buffer;
    }

    @Override
    public void sendPacket(NetworkPacket packet) {
        if (packet instanceof ClientHelloPacket) {
            System.out.println(((ClientHelloPacket) packet).getUsername() + " joined");
        }
        if (packet instanceof MessagePacket) {
            System.out.println(((MessagePacket) packet).getMessage());
        }
    }
}
