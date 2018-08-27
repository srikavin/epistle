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

package infuzion.chat.common.network.packet;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientHelloPacketTest {
    @Test
    void getUuid() {
        UUID uuid = UUID.randomUUID();
        ClientHelloPacket packet = new ClientHelloPacket("asd", uuid);
        assertEquals(uuid, packet.getUuid());

        ClientHelloPacket packet2 = new ClientHelloPacket(packet.asBytes());
        assertEquals(uuid, packet2.getUuid());
    }

    @Test
    void asBytes() {
        UUID uuid = UUID.randomUUID();
        ClientHelloPacket packet = new ClientHelloPacket("asd", uuid);
        ClientHelloPacket packet2 = new ClientHelloPacket(packet.asBytes());
        assertEquals(packet.getUsername(), packet2.getUsername());
        assertEquals(packet.getUuid(), packet2.getUuid());
        assertEquals(packet, packet2);
    }

    @Test
    void getUsername() {
        UUID uuid = UUID.randomUUID();
        ClientHelloPacket packet = new ClientHelloPacket("asd", uuid);
        assertEquals("asd", packet.getUsername());

        ClientHelloPacket packet2 = new ClientHelloPacket(packet.asBytes());
        assertEquals("asd", packet2.getUsername());
    }
}