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

package me.infuzion.chat.server.network.websocket;

import infuzion.chat.common.DataType;
import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.network.ClientConnection;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WebSocketConnection implements ClientConnection {
    private final WebSocket webSocket;
    private final WebSocketHandler webSocketHandler;

    public WebSocketConnection(WebSocket webSocket, WebSocketHandler webSocketHandler) {
        this.webSocket = webSocket;
        this.webSocketHandler = webSocketHandler;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public void close() throws IOException {
        webSocketHandler.removeConnection(webSocket);
    }

    @Override
    public int available() throws IOException {
        return 0;
    }

    @Override
    public ByteBuffer readMessage() throws IOException {
        return ByteBuffer.allocate(0);
    }

    @Override
    public void sendPacket(NetworkPacket packet) throws IOException {
        byte[] data = packet.asBytes();

        // [ Signature ] [  Length  ] [  data  ]  [ End of Packet ]
        //    2 bytes      4 bytes     x bytes         1 byte
        byte[] ret = new byte[data.length + 7];
        ByteBuffer buffer = ByteBuffer.wrap(ret);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        ByteOrder nativeOrder = ByteOrder.nativeOrder();

        buffer.putShort(nativeOrder == ByteOrder.BIG_ENDIAN ? packet.getSignature() : (short) -packet.getSignature());
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.put(DataType.EndOfData.byteValue);

        if (webSocket.isOpen()) {
            webSocket.send(ret);
        } else {
            webSocketHandler.removeConnection(this);
        }
    }
}
