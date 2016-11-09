package infuzion.chat.server.plugin.command;

import infuzion.chat.server.IChatClient;

public interface ICommandExecutor {
    void onCommand(String commandName, String[] args, IChatClient client);

    String[] getHelp();
}
