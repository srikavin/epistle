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
import infuzion.chat.common.network.packet.MessagePacket;
import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.ChatServer;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.NetworkSource;
import me.infuzion.chat.server.api.network.PacketRouter;
import me.infuzion.chat.server.network.handler.CommandPacketHandler;
import me.infuzion.chat.server.network.handler.MessagePacketHandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkManager {
    private final ChatServer server;
    private final List<NetworkSource> sources;
    private PacketRouter packetRouter;

    public NetworkManager(ChatServer server) {
        this(server, new ArrayList<>());
    }

    public NetworkManager(ChatServer server, List<NetworkSource> sources) {
        this.server = server;
        this.sources = sources;
    }

    public void addSource(NetworkSource... sourcesToAdd) {
        sources.addAll(Arrays.asList(sourcesToAdd));
    }

    public PacketRouter getPacketRouter() {
        return packetRouter;
    }

    public void init() {
        sources.forEach(NetworkSource::init);
    }

    public void stop() throws Exception {
        for (NetworkSource source : sources) {
            source.stop();
        }
    }

    public void reload() {
        packetRouter = server.getPacketRouter();
        CommandPacketHandler commandPacketHandler =
                new CommandPacketHandler(server.getCommandManager(), server.getEventManager());
        MessagePacketHandler messagePacketHandler =
                new MessagePacketHandler(server.getChatRoomManager(), server.getEventManager());

        packetRouter.registerNetworkPacketHandler(CommandPacket.class, commandPacketHandler::handle);
        packetRouter.registerNetworkPacketHandler(MessagePacket.class, messagePacketHandler::handle);
        packetRouter.registerNetworkPacketHandler(ClientHelloPacket.class, ((packet, client) -> {
            System.out.println(packet.getUsername() + " has joined!");
        }));
        sources.forEach(NetworkSource::reload);
    }

    public void addClient(IChatClient client) {
        server.getConnectedClients().add(client);
        client.setChatRoom(server.getChatRoomManager().getChatRooms().get(0));
        server.getChatRoomManager().addClient(client);
    }

    public void removeClient(IChatClient client) {
        server.getConnectedClients().remove(client);
        client.getChatRoom().removeClient(client);
    }

    public void enqueue(NetworkPacket message, IChatClient client, Class<? extends NetworkSource> source) {
        server.enqueue(message, client, source);
    }

    public void enqueue(ByteBuffer message, IChatClient client, Class<? extends NetworkSource> source) {
        this.enqueue(packetRouter.parseBuffer(message), client, source);
    }
}
