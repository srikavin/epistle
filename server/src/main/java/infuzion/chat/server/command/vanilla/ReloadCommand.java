package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.Server;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class ReloadCommand implements ICommandExecutor {

    private Server server;

    public ReloadCommand(Server server) {
        this.server = server;
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equals("reload")) {
            server.reload();
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }
}
