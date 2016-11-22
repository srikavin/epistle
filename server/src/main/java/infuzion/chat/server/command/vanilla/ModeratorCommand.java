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

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.ChatRoom;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.permission.IPermissionManager;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class ModeratorCommand implements ICommandExecutor {

    public ModeratorCommand(IServer server) {
        IPermissionManager permissionManager = server.getPermissionManager();
        permissionManager.registerPermission("kick", "chat.kick");
        permissionManager.registerPermission("ban", "chat.ban");
        permissionManager.registerPermission("mute", "chat.mute");
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("kick")) {
            if (args.length >= 1) { //kick [name] [reason...]
                IChatClient toKick = ChatClient.fromName(args[0]);
                if (toKick == null) {
                    client.sendMessage(args[0] + " does not exist!");
                    return;
                }
                if (args.length >= 2) {
                    String message = "";
                    for (int i = 1; i < args.length - 1; i++) {
                        message += " " + args[i];
                    }
                    client.sendMessage("You kicked " + toKick.getName().trim() + " for " + message.trim());
                    ChatRoom.getChatRoomManager().kickClient(toKick, message.trim());
                }
                ChatRoom.getChatRoomManager().kickClient(toKick);
            } else {
                sendHelp(client);
            }
        }
    }

    private void sendHelp(IChatClient client) {
        for (String e : getHelp()) {
            client.sendMessage(e);
        }
    }

    public String getPrefix() {
        return "Moderator";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "/kick [name]"
        };
    }
}
