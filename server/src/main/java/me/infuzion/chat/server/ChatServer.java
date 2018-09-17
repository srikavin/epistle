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

package me.infuzion.chat.server;

import com.esotericsoftware.yamlbeans.YamlException;
import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoomManager;
import me.infuzion.chat.server.api.Server;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;
import me.infuzion.chat.server.api.network.NetworkSource;
import me.infuzion.chat.server.api.network.PacketRouter;
import me.infuzion.chat.server.api.permission.IPermissionManager;
import me.infuzion.chat.server.api.plugin.loader.IPluginManager;
import me.infuzion.chat.server.command.CommandManager;
import me.infuzion.chat.server.event.EventManager;
import me.infuzion.chat.server.network.DefaultPacketRouter;
import me.infuzion.chat.server.network.NetworkManager;
import me.infuzion.chat.server.network.socket.SocketHandler;
import me.infuzion.chat.server.network.websocket.WebSocketHandler;
import me.infuzion.chat.server.permission.def.DefaultPermissionManager;
import me.infuzion.chat.server.plugin.loader.PluginManager;
import me.infuzion.chat.server.util.QueuedServerPacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatServer implements Server {
    private final File permissionFile = new File("permissions.yml");
    private IChatRoomManager chatRoomManager;
    private IPluginManager pluginManager;
    private ICommandManager commandManager;
    private IEventManager eventManager;
    private IPermissionManager permissionManager;
    private int tps = 20;
    private long tpsTotal = 0;
    private int tpsCounter = 0;
    private List<IChatClient> clients = new ArrayList<>();
    private Queue<QueuedServerPacket> packetQueue = new ConcurrentLinkedQueue<>();
    private NetworkManager networkManager;
    private DefaultPacketRouter packetRouter = new DefaultPacketRouter();

    ChatServer(int port) throws IOException {
        chatRoomManager = new ChatRoomManager();
        IChatClient serverClient = new ServerChatClient();
        clients.add(serverClient);
        chatRoomManager.addClient(serverClient);
        pluginManager = new PluginManager(this);
        eventManager = new EventManager(this);

        networkManager = new NetworkManager(this);
        networkManager.addSource(new SocketHandler(networkManager, port));
        networkManager.addSource(new WebSocketHandler(networkManager));
        networkManager.init();

        reload();
    }

    public PacketRouter getPacketRouter() {
        return packetRouter;
    }

    public IChatRoomManager getChatRoomManager() {
        return chatRoomManager;
    }

    @Override
    public void enqueue(NetworkPacket packet, IChatClient client, Class<? extends NetworkSource> source) {
        if (source == null) {
            source = NetworkSource.class;
        }

        packetQueue.add(new QueuedServerPacket(packet, client, source));
    }

    public List<IChatClient> getClients() {
        return clients;
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    QueuedServerPacket packet = packetQueue.poll();
                    if (packet == null) {
                        Thread.sleep(100);
                        continue;
                    }
                    packetRouter.handleNetworkPacket(packet.getPacket(), packet.getClient(), packet.getSource());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setName("Main Thread");
        thread.start();
    }

    public void reload() {
        pluginManager.disable();
        try {
            File file = new File("plugins");
            //noinspection ResultOfMethodCallIgnored
            file.mkdir();
            pluginManager = new PluginManager(this);
            eventManager.reset();
            try {
                permissionFile.createNewFile();
                permissionManager = new DefaultPermissionManager(this,
                        new InputStreamReader(new FileInputStream(permissionFile)));
            } catch (YamlException e) {
                System.out.println("Errors in permissions.yml");
                e.printStackTrace();
                System.exit(0);
            }

            //Init this last
            commandManager = new CommandManager(this);
            pluginManager.addAllPlugins(file);
            pluginManager.enable();

            packetRouter = new DefaultPacketRouter();
            networkManager.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        pluginManager.disable();
        System.exit(0);
    }

    public IPluginManager getPluginManager() {
        return pluginManager;
    }

    public IEventManager getEventManager() {
        return eventManager;
    }

    public ICommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(IPermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public int getTps() {
        return tps;
    }

    @Override
    public long getTotalTps() {
        return tpsTotal;
    }

    @Override
    public List<IChatClient> getConnectedClients() {
        return clients;
    }
}
