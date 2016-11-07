package infuzion.chat.server.command;

import infuzion.chat.server.ChatClient;

public interface ICommandExecutor {
    void onCommand(String commandName, String[] args, ChatClient client);

    String getPrefix();
}
