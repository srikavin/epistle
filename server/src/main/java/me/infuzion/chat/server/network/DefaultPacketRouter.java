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
import me.infuzion.chat.server.api.network.NetworkSource;
import me.infuzion.chat.server.api.network.PacketRouter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.BiConsumer;

public class DefaultPacketRouter implements PacketRouter {
    private Map<Short, PacketParser> packetMappings = new HashMap<>();
    private Map<Short, List<BiConsumer<? extends NetworkPacket, IChatClient>>> packetSignatureHandlers =
            new HashMap<>();
    private Map<Class<? extends NetworkSource>, List<NetworkPacketHandler<? extends NetworkPacket>>> packetHandlers =
            new HashMap<>();

    public DefaultPacketRouter() {
        this.registerNetworkPacketMappings(Arrays.asList(
                new ClientHelloPacketFactory(),
                new CommandPacketFactory(),
                new MessagePacketFactory(),
                new KeepAlivePacketFactory()
        ));
    }

    public DefaultPacketRouter(List<PacketParser> packetMappings) {
        this.registerNetworkPacketMappings(packetMappings);
    }

    private void registerNetworkPacketMappings(List<PacketParser> mappings) {
        mappings.forEach((e) -> packetMappings.put(e.getSignature(), e));
    }

    @Override
    public <T extends NetworkPacket> void registerNetworkPacketHandler(Class<T> packet, BiConsumer<T, IChatClient> method) {
        this.registerNetworkPacketHandler(packet, method, NetworkSource.class);
    }

    @Override
    public <T extends NetworkPacket> void registerNetworkPacketHandler(Class<T> packet, BiConsumer<T, IChatClient> method, Class<? extends NetworkSource> source) {
        packetHandlers.computeIfAbsent(source, (c) -> new ArrayList<>());
        packetHandlers.get(source).add(new NetworkPacketHandler<>(packet, method));

    }

    @Override
    public <T extends NetworkPacket> void registerNetworkPacketHandler(short signature, BiConsumer<T, IChatClient> method) {
        packetSignatureHandlers.computeIfAbsent(signature, (s) -> new ArrayList<>());
        packetSignatureHandlers.get(signature).add(method);
    }

    @Override
    public void sendNetworkPacket(ClientConnection connection, NetworkPacket packet) throws IOException {
        connection.sendPacket(packet);
    }

    @Override
    public NetworkPacket parseBuffer(ByteBuffer packetBuffer) {
        short signature = packetBuffer.getShort();
        PacketParser packetParser = packetMappings.get(signature);

        if (packetParser == null) {
            System.out.println(signature);
            throw new RuntimeException("Unknown packet signature");
        }

        return packetParser.parse(packetBuffer);
    }

    @Override
    public void handleNetworkPacket(ByteBuffer packetBuffer, IChatClient client) {
        NetworkPacket packet = parseBuffer(packetBuffer);
        handleNetworkPacket(packet, client);
    }

    @Override
    public void handleNetworkPacket(NetworkPacket packet, IChatClient client) {
        this.handleNetworkPacket(packet, client, NetworkSource.class);
    }

    @Override
    public void handleNetworkPacket(NetworkPacket packet, IChatClient client, Class<? extends NetworkSource> source) {
        packetHandlers.forEach((desiredSource, list) -> {
            if (!desiredSource.isAssignableFrom(source)) {
                return;
            }

            list.forEach((e) -> {
                Class<? extends NetworkPacket> handlerClass = e.packetClass;
                if (handlerClass.isAssignableFrom(packet.getClass())) {

                    //noinspection unchecked
                    ((BiConsumer<NetworkPacket, IChatClient>) e.handler).accept(packet, client);
                }
            });
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
