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

import me.infuzion.chat.server.ChatRoom;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoomManager;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.DefaultCommand;

public class ChatRoomCommand implements VanillaCommandExecutor {

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("chatroom") || commandName.equalsIgnoreCase("cr")) {
            final String prefix = "[ChatRoom] ";
            if (args.length < 2) {
                client.sendMessage(prefix + "Invalid Usage!");
                sendHelp(client);
                return;
            }
            IChatRoomManager roomManager = ChatRoom.getChatRoomManager();
            if (args[0].equalsIgnoreCase("create")) {
                if (args[1] != null) {
                    roomManager.addChatRoom(new ChatRoom(args[1]));
                    client.sendMessage(prefix + "Success! A chat room (" + args[1] + ") has been created!");
                } else {
                    sendHelp(client);
                }
            } else if (args[0].equalsIgnoreCase("move")) {
                if (roomManager.fromString(args[1]) != null) {
                    roomManager.moveClient(client, roomManager.fromString(args[1]));
                    client.sendMessage(prefix + "Success! You have been moved into " + args[1]);
                } else {
                    client.sendMessage(prefix + "Invalid Usage!");
                    sendHelp(client);
                }
            }
        }
    }

    public String[] getHelp() {
        return new String[]{
                "Aliases: /cr",
                "/chatroom create [name] - Creates a chatroom with the specified name",
                "/chatroom move [name] -   Moves you into the  chatroom with the specified name"
        };
    }

    private void sendHelp(IChatClient client) {
        for (String e : getHelp()) {
            client.sendMessage(e);
        }
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new DefaultCommand("cr"),
                new DefaultCommand("chatroom"),
                new DefaultCommand("create"),
                new DefaultCommand("move")
        };
    }
}
