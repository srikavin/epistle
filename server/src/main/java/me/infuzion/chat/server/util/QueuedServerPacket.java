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

package me.infuzion.chat.server.util;

import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.network.NetworkSource;

public class QueuedServerPacket {
    private final NetworkPacket packet;
    private final IChatClient client;
    private final Class<? extends NetworkSource> source;

    public QueuedServerPacket(NetworkPacket packet, IChatClient client, Class<? extends NetworkSource> source) {
        this.packet = packet;
        this.client = client;
        this.source = source;
    }

    public IChatClient getClient() {
        return client;
    }

    public Class<? extends NetworkSource> getSource() {
        return source;
    }

    public NetworkPacket getPacket() {
        return packet;
    }
}
