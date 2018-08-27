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

package infuzion.chat.common.network.packet.factory;

import infuzion.chat.common.network.packet.ClientHelloPacket;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ClientHelloPacketFactory extends NetworkPacketFactory<ClientHelloPacket> {
    private String username;
    private UUID uuid;

    @Override
    public ClientHelloPacket parse(ByteBuffer packet) {
        return new ClientHelloPacket(packet);
    }

    @Override
    public short getSignature() {
        return ClientHelloPacket.signature.value;
    }

    @Override
    public ClientHelloPacket build() {
        return new ClientHelloPacket(username, uuid);
    }

    public String getUsername() {
        return username;
    }

    public ClientHelloPacketFactory setUsername(String username) {
        this.username = username;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ClientHelloPacketFactory setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }
}
