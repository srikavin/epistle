package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.ChatRoom;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IChatRoomManager;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class ChatRoomCommand implements ICommandExecutor {
    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("chatroom") || commandName.equalsIgnoreCase("cr")) {
            if (args.length < 2) {
                client.sendMessage(getPrefix() + "Invalid Usage!");
                sendHelp(client);
                return;
            }
            IChatRoomManager roomManager = ChatRoom.getChatRoomManager();
            if (args[0].equalsIgnoreCase("create")) {
                if (args[1] != null) {
                    roomManager.addChatRoom(new ChatRoom(args[1]));
                    client.sendMessage("Success! A chat room (" + args[1] + ") has been created!");
                } else {
                    sendHelp(client);
                }
            } else if (args[0].equalsIgnoreCase("move")) {
                if (roomManager.fromString(args[1]) != null) {
                    roomManager.moveClient(client, roomManager.fromString(args[1]));
                    client.sendMessage("Success! You have been moved into " + args[1]);
                } else {
                    sendHelp(client);
                }
            }
        }
    }

    public String getPrefix() {
        return "[ChatRoom] ";
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
}
