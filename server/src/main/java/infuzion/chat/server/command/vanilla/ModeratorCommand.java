package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.ChatRoom;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class ModeratorCommand implements ICommandExecutor {

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
                        message += args[i];
                    }
                    client.sendMessage("You kicked " + toKick.getName() + " for " + message);
                    ChatRoom.getChatRoomManager().kickClient(toKick, message);
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
