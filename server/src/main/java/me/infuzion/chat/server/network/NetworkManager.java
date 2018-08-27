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

package me.infuzion.chat.server.network;

import infuzion.chat.common.network.packet.ClientHelloPacket;
import infuzion.chat.common.network.packet.CommandPacket;
import infuzion.chat.common.network.packet.KeepAlivePacket;
import infuzion.chat.common.network.packet.MessagePacket;
import me.infuzion.chat.server.ChatClient;
import me.infuzion.chat.server.ChatServer;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.ClientConnection;
import me.infuzion.chat.server.network.handler.CommandPacketHandler;
import me.infuzion.chat.server.network.handler.MessagePacketHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkManager {
    private static final int CLIENT_TIMEOUT_MILLISECONDS = 15000;
    private final Map<IChatClient, Integer> heartbeat = new HashMap<>();
    private final ChatServer server;
    private ServerSocket socket;
    private List<ClientConnection> joiningClients = new CopyOnWriteArrayList<>();
    private volatile boolean running = true;
    private PacketRouter packetRouter;
    private ClientConnection lastJoined;

    public NetworkManager(ChatServer server, int port) throws IOException {
        socket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
        System.out.println("Bound to port " + socket.getLocalPort() + " at " + socket.getInetAddress().toString());

        this.server = server;
        reload();
    }

    public void init() {
        Thread mainThread = new Thread(this::mainNetwork);
        mainThread.setName("Chat Network Manager");
        mainThread.start();

        Thread connectionsThread = new Thread(this::handleConnections);
        connectionsThread.setName("Connection mainThread");
        connectionsThread.start();

        Thread keepAliveThread = new Thread(this::keepAlive);
        keepAliveThread.setName("Keep Alive Thread");
        keepAliveThread.start();
    }

    private void keepAlive() {
        while (running) {
            heartbeat.forEach((client, integer) -> {
                if (client.isConsole()) {
                    return;
                }
                if (integer <= 0) {
                    client.kick("timed out");
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void mainNetwork() {
        while (running) {
            List<IChatClient> toRemove = new ArrayList<>();
            for (IChatClient client : server.getClients()) {
                try {
                    ClientConnection connection = client.getConnection();

                    if (connection.available() < 1) {
                        continue;
                    }

                    ByteBuffer packet = connection.readMessage();
                    packetRouter.handleNetworkPacket(packet, client);

                    heartbeat.put(client, CLIENT_TIMEOUT_MILLISECONDS);

                } catch (Exception e) {
                    e.printStackTrace();
                    toRemove.add(client);
                }
            }

            server.getClients().removeAll(toRemove);
            toRemove.clear();

            List<ClientConnection> joiningToRemove = new ArrayList<>();
            for (ClientConnection connection : joiningClients) {
                try {
                    ByteBuffer message;
                    message = connection.readMessage();
                    lastJoined = connection;
                    packetRouter.handleNetworkPacket(message, null);
                    joiningToRemove.add(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                    joiningToRemove.add(connection);
                }
            }
            joiningClients.removeAll(joiningToRemove);
            joiningToRemove.clear();

            try {
                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConnections() {
        try {
            while (running) {
                Socket client = socket.accept();
                joiningClients.add(new SocketConnection(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }

    public void reload() {
        packetRouter = new PacketRouter();
        CommandPacketHandler commandPacketHandler =
                new CommandPacketHandler(server.getCommandManager(), server.getEventManager());
        MessagePacketHandler messagePacketHandler =
                new MessagePacketHandler(server.getChatRoomManager(), server.getEventManager());

        packetRouter.registerNetworkPacketHandler(CommandPacket.class, commandPacketHandler::handle);
        packetRouter.registerNetworkPacketHandler(MessagePacket.class, messagePacketHandler::handle);
        packetRouter.registerNetworkPacketHandler(ClientHelloPacket.class, (this::handleClientHelloPacket));
        packetRouter.registerNetworkPacketHandler(KeepAlivePacket.class, (this::handleKeepAlivePacker));
    }

    private void handleClientHelloPacket(ClientHelloPacket clientHelloPacket, IChatClient client) {
        ChatClient newClient = new ChatClient(clientHelloPacket.getUsername(), clientHelloPacket.getUuid(), lastJoined);
        server.getClients().add(newClient);

        newClient.setChatRoom(server.getChatRoomManager().getChatRooms().get(0));
        server.getChatRoomManager().addClient(newClient);
    }

    private void handleKeepAlivePacker(KeepAlivePacket packet, IChatClient client) {
        heartbeat.put(client, NetworkManager.CLIENT_TIMEOUT_MILLISECONDS);
    }
}
