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

import infuzion.chat.common.network.packet.ClientHelloPacket;
import me.infuzion.chat.server.ChatClient;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.NetworkSource;
import me.infuzion.chat.server.network.NetworkManager;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebSocketHandler implements NetworkSource {
    private final Queue<WebSocket> joinedQueue = new ConcurrentLinkedQueue<>();
    private final Map<WebSocketConnection, IChatClient> connectionMap = new HashMap<>();
    private final NetworkManager manager;
    private final InetSocketAddress socketAddress;
    private WebSocketListener webSocketListener;

    public WebSocketHandler(NetworkManager manager, int port) {
        this.manager = manager;
        this.socketAddress = new InetSocketAddress("0.0.0.0", port);
    }

    @Override
    public void init() {
        webSocketListener = new WebSocketListener(this, socketAddress);
        webSocketListener.start();
        System.out.println("Listening for websocket connections on port " + webSocketListener.getPort());
    }

    @Override
    public void reload() {
        manager.getPacketRouter().registerNetworkPacketHandler(ClientHelloPacket.class, (packet, client) -> {
            WebSocket webSocket = joinedQueue.remove();
            WebSocketConnection connection = new WebSocketConnection(webSocket, this);
            IChatClient newClient = new ChatClient(packet.getUsername(), packet.getUuid(), connection);
            manager.addClient(newClient);
            webSocket.setAttachment(newClient);
            connectionMap.put(connection, newClient);
        }, getClass());
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        webSocketListener.stop();
    }

    void onJoin(WebSocket socket, ByteBuffer buffer) {
        joinedQueue.add(socket);
        this.enqueue(buffer, null);
    }

    void enqueue(ByteBuffer buffer, IChatClient client) {
        System.out.println("enqueuing");
        manager.enqueue(buffer, client, getClass());
    }

    public void removeConnection(WebSocketConnection webSocket) {
        IChatClient client = connectionMap.get(webSocket);
        manager.removeClient(client);
        webSocketListener.removeConnection(webSocket.getWebSocket());
    }

    public void removeConnection(WebSocket conn) {
        webSocketListener.removeConnection(conn);
    }
}
