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

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WebSocketListener extends WebSocketServer {
    private final WebSocketHandler handler;

    public WebSocketListener(WebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        handler.removeConnection(webSocket);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        conn.send("Wrong protocol");
        handler.removeConnection(conn);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();

        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() - 5);

        short sig = buffer.getShort();
        byte[] contents = new byte[buffer.getInt()];
        buffer.get(contents);
        if (buffer.get() != 2) {
            throw new RuntimeException("Invalid packet received!");
        }

        System.out.println(contents.length);
        newBuffer.order(ByteOrder.LITTLE_ENDIAN);
        newBuffer.putShort(sig);
        newBuffer.put(contents);

        newBuffer.rewind();

        if (webSocket.getAttachment() == null) {
            handler.onJoin(webSocket, newBuffer);
            return;
        }

        handler.enqueue(newBuffer, webSocket.getAttachment());
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        handler.removeConnection(webSocket);
        e.printStackTrace();
    }

    @Override
    public void onStart() {

    }

    public boolean removeConnection(WebSocket socket) {
        socket.close();
        return super.removeConnection(socket);
    }
}
