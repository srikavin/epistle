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

package me.infuzion.chat.server.network.handler;

import infuzion.chat.common.DataType;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.ClientConnection;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class NetworkHandler {
    public void handlePacket(IChatClient client, ByteBuffer buffer) {
        DataType type = DataType.valueOf(buffer.get());
        if (type == null) {
            throw new RuntimeException("Unknown packet");
        }
        System.out.println(type);

        switch (type) {
            case Message:
                this.handleMessage(client, buffer);
                break;
            case EndOfData:
                return;
            case ColorChange:
                this.handleColorChange(client, buffer);
                break;
            case Heartbeat:
                this.handleKeepAlive(client, buffer);
                break;
            case Command:
                this.handleCommand(client, buffer);
                break;
            case Kick:
                this.handleKick(client, buffer);
                break;
            default:
                throw new RuntimeException("Unexpected packet type");
        }
    }

    public abstract void handleKeepAlive(IChatClient client, ByteBuffer buffer);

    public abstract void handleMessage(IChatClient client, ByteBuffer buffer);

    public abstract void handleCommand(IChatClient client, ByteBuffer buffer);

    public abstract void handleColorChange(IChatClient client, ByteBuffer buffer);

    public abstract void handleKick(IChatClient client, ByteBuffer buffer);

    public abstract IChatClient handleHello(ClientConnection connection, ByteBuffer buffer);

    public void expect(byte expected, ByteBuffer buffer) {
        if (buffer.get() != expected) {
            throw new RuntimeException("Invalid network packet received");
        }
    }

    public String readString(int size, ByteBuffer buffer) {
        byte[] string = new byte[size];
        buffer.get(string, 0, size);
        return new String(string, StandardCharsets.UTF_8);
    }
}
