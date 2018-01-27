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

package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.ChatRoom;
import infuzion.chat.server.event.chat.ChatEvent;
import infuzion.chat.server.event.reflection.EventHandler;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IServer;
import me.infuzion.chat.server.api.command.ICommandExecutor;
import me.infuzion.chat.server.api.event.IEventListener;
import me.infuzion.chat.server.api.event.reflection.EventPriority;
import me.infuzion.chat.server.api.permission.IPermissionManager;

public class ModeratorCommand implements ICommandExecutor, IEventListener {
    public ModeratorCommand(IServer server) {
        IPermissionManager permissionManager = server.getPermissionManager();
        server.getEventManager().registerListener(this, null);
        permissionManager.registerPermission("kick", "chat.kick");
        permissionManager.registerPermission("ban", "chat.ban");
        permissionManager.registerPermission("mute", "chat.mute");
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("kick")) {
            kick(args, client);
        }
    }

    private void kick(String[] args, IChatClient client) {
        if (args.length >= 1) { //kick [name] [reason...]
            IChatClient toKick = ChatClient.fromName(args[0]);
            if (toKick == null) {
                client.sendMessage(args[0] + " does not exist!");
                return;
            }
            if (args.length >= 2) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(" ").append(args[i]);
                }
                client.sendMessage(
                        getPrefix() + "You kicked " + toKick.getName().trim() + " for " + message.toString()
                                .trim());
                ChatRoom.getChatRoomManager().kickClient(toKick, message.toString().trim());
            } else {
                ChatRoom.getChatRoomManager().kickClient(toKick);
            }
        } else {
            sendHelp(client);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(ChatEvent e) {
        if (!e.isCanceled()) {

        }
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "/kick [name]"
        };
    }

    private void sendHelp(IChatClient client) {
        for (String e : getHelp()) {
            client.sendMessage(e);
        }
    }

    @SuppressWarnings("SameReturnValue")
    public String getPrefix() {
        return "Moderator";
    }
}
