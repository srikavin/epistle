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

package me.infuzion.chat.server.mock;

import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoomManager;
import me.infuzion.chat.server.api.Server;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;
import me.infuzion.chat.server.api.network.NetworkSource;
import me.infuzion.chat.server.api.permission.IPermissionManager;
import me.infuzion.chat.server.api.plugin.loader.IPluginManager;

import java.util.List;

public class FakeServer implements Server {
    public boolean stopCalled = false;
    private int tps = -1;
    private long totalTPS = -1;

    public void reset() {
        stopCalled = false;
    }

    @Override
    public void reload() {
    }

    @Override
    public void stop() {
        stopCalled = true;
    }

    public void setTps(int tps, long totalTPS) {
        this.tps = tps;
        this.totalTPS = totalTPS;
    }

    @Override
    public IPluginManager getPluginManager() {
        return null;
    }

    @Override
    public IEventManager getEventManager() {
        return new FakeEventManager();
    }

    @Override
    public ICommandManager getCommandManager() {
        return new FakeCommandManager();
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return new FakePermissionManager();
    }

    @Override
    public void setPermissionManager(IPermissionManager permissionManager) {

    }

    @Override
    public void enqueue(NetworkPacket packet, IChatClient client, Class<? extends NetworkSource> source) {

    }

    @Override
    public IChatRoomManager getChatRoomManager() {
        return null;
    }

    @Override
    public int getTps() {
        return tps;
    }

    @Override
    public long getTotalTps() {
        return totalTPS;
    }

    @Override
    public List<IChatClient> getConnectedClients() {
        return null;
    }
}
