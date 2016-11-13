package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class TpsCommand implements ICommandExecutor {

    private IServer server;

    public TpsCommand(IServer server) {
        this.server = server;
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("tps")) {
            client.sendMessage("The server is running at " + server.getTps() + " ticks per second.");
            client.sendMessage("The server has run for a total of " + server.getTotalTps() + " ticks.");
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }
}
