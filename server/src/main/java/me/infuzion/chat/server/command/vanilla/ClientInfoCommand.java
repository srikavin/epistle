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

package me.infuzion.chat.server.command.vanilla;

import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.Server;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.DefaultCommand;
import me.infuzion.chat.server.network.socket.SocketConnection;
import me.infuzion.chat.server.network.websocket.WebSocketConnection;

import java.util.UUID;

public class ClientInfoCommand implements VanillaCommandExecutor {

    private final Server server;

    public ClientInfoCommand(Server server) {
        this.server = server;
        server.getPermissionManager().registerPermission("clientinfo", "chat.info");
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("clientinfo")) {
            if (args.length > 0) {
                String clientToCheck = args[0];
                UUID uuid = null;
                String chatroom = null;
                String ip = "unknown";
                String name = null;
                for (IChatClient e : server.getConnectedClients()) {
                    if (e.getName().equalsIgnoreCase(clientToCheck)) {
                        uuid = e.getUuid();
                        name = e.getName();
                        chatroom = e.getChatRoom().getName();
                        if (e.getConnection() instanceof SocketConnection) {
                            ip = ((SocketConnection) e.getConnection()).getSocket().getInetAddress().toString();
                        }
                        if (e.getConnection() instanceof WebSocketConnection) {
                            ip = ((WebSocketConnection) e.getConnection())
                                    .getWebSocket().getRemoteSocketAddress().getAddress().getHostAddress();
                        }
                    }
                }
                if (uuid != null) {
                    client.sendMessage("Name: " + name);
                    client.sendMessage("UUID: " + uuid);
                    client.sendMessage("Chat Room: " + chatroom);
                    client.sendMessage("Ip: " + ip);
                }
            }
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new DefaultCommand("clientinfo")
        };
    }
}
