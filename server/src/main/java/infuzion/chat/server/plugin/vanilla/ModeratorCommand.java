package infuzion.chat.server.plugin.vanilla;

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.plugin.ICommandExecutor;

public class ModeratorCommand implements ICommandExecutor {

    @Override
    public void onCommand(String commandName, String[] args, ChatClient client) {
        this.getClass().getClassLoader();
    }

    public String getPrefix() {
        return "Moderator";
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }
}
