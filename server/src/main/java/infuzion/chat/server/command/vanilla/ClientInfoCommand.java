/*
 *
 *  *  Copyright 2016 Infuzion
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.plugin.command.ICommandExecutor;

import java.util.UUID;

public class ClientInfoCommand implements ICommandExecutor {

    private final IServer server;

    public ClientInfoCommand(IServer server) {
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
                String ip = null;
                String name = null;
                for (IChatClient e : server.getConnectedClients()) {
                    if (e.getName().equalsIgnoreCase(clientToCheck)) {
                        uuid = e.getUuid();
                        name = e.getName();
                        chatroom = e.getChatRoom().getName();
                        ip = e.getSocket().getInetAddress().toString();
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
}