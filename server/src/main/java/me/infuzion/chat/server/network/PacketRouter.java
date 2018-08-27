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

import infuzion.chat.common.network.packet.NetworkPacket;
import infuzion.chat.common.network.packet.PacketParser;
import infuzion.chat.common.network.packet.factory.ClientHelloPacketFactory;
import infuzion.chat.common.network.packet.factory.CommandPacketFactory;
import infuzion.chat.common.network.packet.factory.KeepAlivePacketFactory;
import infuzion.chat.common.network.packet.factory.MessagePacketFactory;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.ClientConnection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.BiConsumer;

public class PacketRouter {
    private Map<Short, PacketParser> packetMappings = new HashMap<>();
    private Map<Short, List<BiConsumer<? extends NetworkPacket, IChatClient>>> packetSignatureHandlers =
            new HashMap<>();
    private List<NetworkPacketHandler<? extends NetworkPacket>> packetHandlers = new ArrayList<>();

    public PacketRouter() {
        this.registerNetworkPacketMappings(Arrays.asList(
                new ClientHelloPacketFactory(),
                new CommandPacketFactory(),
                new MessagePacketFactory(),
                new KeepAlivePacketFactory()
        ));
    }

    public PacketRouter(List<PacketParser> packetMappings) {
        this.registerNetworkPacketMappings(packetMappings);
    }

    private void registerNetworkPacketMappings(List<PacketParser> mappings) {
        mappings.forEach((e) -> packetMappings.put(e.getSignature(), e));
    }

    public <T extends NetworkPacket> void registerNetworkPacketHandler(Class<T> packet, BiConsumer<T, IChatClient> method) {
        packetHandlers.add(new NetworkPacketHandler<>(packet, method));
    }

    public <T extends NetworkPacket> void registerNetworkPacketHandler(short signature, BiConsumer<T, IChatClient> method) {
        packetSignatureHandlers.computeIfAbsent(signature, (s) -> new ArrayList<>());
        packetSignatureHandlers.get(signature).add(method);
    }

    public void sendNetworkPacket(ClientConnection connection, NetworkPacket packet) throws IOException {
        connection.sendPacket(packet);
    }

    public void handleNetworkPacket(ByteBuffer packetBuffer, IChatClient client) {
        short signature = packetBuffer.getShort();
        PacketParser packetParser = packetMappings.get(signature);

        if (packetParser == null) {
            System.out.println(signature);
//            throw new RuntimeException("Unknown packet signature");
            return;
        }

        NetworkPacket packet = packetParser.parse(packetBuffer);
        handleNetworkPacket(packet, client);
    }

    public void handleNetworkPacket(NetworkPacket packet, IChatClient client) {
        packetHandlers.forEach(e -> {
            Class<? extends NetworkPacket> handlerClass = e.packetClass;
            if (handlerClass.isAssignableFrom(packet.getClass())) {

                //noinspection unchecked
                ((BiConsumer<NetworkPacket, IChatClient>) e.handler).accept(packet, client);
            }
        });

        packetSignatureHandlers
                .entrySet()
                .stream()
                .filter(e -> e.getKey() == packet.getSignature())
                .forEach(e -> e.getValue().forEach((consumer) -> {
                    //noinspection unchecked
                    ((BiConsumer<NetworkPacket, IChatClient>) consumer).accept(packet, client);
                }));
    }

    static class NetworkPacketHandler<T extends NetworkPacket> {
        Class<T> packetClass;
        BiConsumer<? super T, IChatClient> handler;

        public NetworkPacketHandler(Class<T> packetClass, BiConsumer<? super T, IChatClient> handler) {
            this.packetClass = packetClass;
            this.handler = handler;
        }
    }
}
