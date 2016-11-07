package infuzion.chat.server.command;

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.ChatRoom;
import infuzion.chat.server.ChatRoomManager;

public class ChatRoomCommand implements ICommandExecutor {
    @Override
    public void onCommand(String commandName, String[] args, ChatClient client) {
        if (args.length < 2) {
            client.sendMessage(getPrefix() + "Invalid Usage!");
        }
        if (commandName.equalsIgnoreCase("chatroom")) {
            ChatRoomManager roomManager = ChatRoom.getChatRoomManager();
            if (args[0].equalsIgnoreCase("add")) {
                roomManager.addChatRoom(new ChatRoom(args[1]));
                client.sendMessage("Success! A chat room (" + args[1] + ") has been created!");
            } else if (args[0].equalsIgnoreCase("move")) {
                if (roomManager.fromString(args[1]) != null) {
                    roomManager.moveClient(client, roomManager.fromString(args[1]));
                    client.sendMessage("Success! You have been moved into " + args[1]);
                }
            }
        }
    }

    @Override
    public String getPrefix() {
        return "[ChatRoom] ";
    }
}
