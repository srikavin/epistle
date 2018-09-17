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

package me.infuzion.chat.server.network.socket;

import infuzion.chat.common.network.packet.ClientHelloPacket;
import infuzion.chat.common.network.packet.KeepAlivePacket;
import me.infuzion.chat.server.ChatClient;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.ClientConnection;
import me.infuzion.chat.server.api.network.NetworkSource;
import me.infuzion.chat.server.api.network.PacketRouter;
import me.infuzion.chat.server.network.NetworkManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketHandler implements NetworkSource {
    private static final int CLIENT_TIMEOUT_MILLISECONDS = 15000;
    private final Map<IChatClient, Integer> heartbeat = new HashMap<>();
    private final NetworkManager networkManager;
    private ServerSocket socket;
    private volatile boolean running = true;
    private Queue<ClientConnection> lastJoined = new ConcurrentLinkedQueue<>();
    private List<IChatClient> clients = new ArrayList<>();
    private List<ClientConnection> joiningClients = new CopyOnWriteArrayList<>();

    public SocketHandler(NetworkManager networkManager, int port) throws IOException {
        this.networkManager = networkManager;
        socket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
        System.out.println("Bound to port " + socket.getLocalPort() + " at " + socket.getInetAddress().toString());
    }

    @Override
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
        List<IChatClient> toRemove = new ArrayList<>();
        while (running) {
            heartbeat.forEach((client, integer) -> {
                if (client.isConsole()) {
                    return;
                }
                if (integer <= 0) {
                    toRemove.add(client);
                    client.kick("timed out");
                    networkManager.removeClient(client);
                }
            });
            toRemove.forEach(heartbeat::remove);
            toRemove.clear();
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
            for (IChatClient client : clients) {
                try {
                    ClientConnection connection = client.getConnection();

                    if (connection.available() < 1) {
                        continue;
                    }

                    ByteBuffer packet = connection.readMessage();
                    networkManager.enqueue(packet, client, getClass());

                    heartbeat.put(client, CLIENT_TIMEOUT_MILLISECONDS);

                } catch (Exception e) {
                    e.printStackTrace();
                    toRemove.add(client);
                }
            }

            clients.removeAll(toRemove);
            toRemove.clear();

            List<ClientConnection> joiningToRemove = new ArrayList<>();
            for (ClientConnection connection : joiningClients) {
                try {
                    ByteBuffer message;
                    message = connection.readMessage();
                    lastJoined.add(connection);
                    networkManager.enqueue(message, null, getClass());
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

    @Override
    public void reload() {
        PacketRouter packetRouter = networkManager.getPacketRouter();
        packetRouter.registerNetworkPacketHandler(ClientHelloPacket.class, (this::handleClientHelloPacket), getClass());
        packetRouter.registerNetworkPacketHandler(KeepAlivePacket.class, (this::handleKeepAlivePacker), getClass());
    }

    private void handleClientHelloPacket(ClientHelloPacket clientHelloPacket, IChatClient client) {
        ClientConnection connection = lastJoined.remove();
        ChatClient newClient = new ChatClient(clientHelloPacket.getUsername(), clientHelloPacket.getUuid(), connection);
        clients.add(newClient);
        networkManager.addClient(newClient);
    }

    private void handleKeepAlivePacker(KeepAlivePacket packet, IChatClient client) {
        heartbeat.put(client, SocketHandler.CLIENT_TIMEOUT_MILLISECONDS);
    }

    @Override
    public void stop() throws Exception {
        running = false;
    }
}
