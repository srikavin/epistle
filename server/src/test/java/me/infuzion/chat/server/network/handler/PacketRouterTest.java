/*
 * Copyright 2018 Srikavin Ramkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain parser copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.infuzion.chat.server.network.handler;

import infuzion.chat.common.network.packet.NetworkPacket;
import infuzion.chat.common.network.packet.PacketParser;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.network.PacketRouter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PacketRouterTest {
    @Mock
    private NetworkPacket packet;
    @Mock
    private PacketParser parser;
    @Mock
    private IChatClient client;

    private PacketRouter router;

    @Before
    public void createInstance() {
        List<PacketParser> handlers = new ArrayList<>();

        handlers.add(parser);

        router = new PacketRouter(handlers);
    }

    @Test
    public void registerNetworkPacketHandler() {
        AtomicBoolean called = new AtomicBoolean(false);
        router.registerNetworkPacketHandler(packet.getClass(), (o, c) -> called.set(true));

        router.handleNetworkPacket(packet, client);
        assertTrue(called.get());
    }

    @Test
    public void registerNetworkPacketHandler1() {
        when(packet.getSignature()).thenReturn((short) 123);
        AtomicBoolean called = new AtomicBoolean(false);
        router.registerNetworkPacketHandler((short) 123, (o, c) -> called.set(true));

        router.handleNetworkPacket(packet, client);
        assertTrue(called.get());
    }

    @Test
    public void sendNetworkPacket() {
    }

    @Test
    public void handleNetworkPacket() {
        router.registerNetworkPacketHandler(packet.getClass(), (o, c) -> assertEquals(packet, o));

        router.handleNetworkPacket(packet, client);
    }
}