package infuzion.chat.server.command;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.Server;
import infuzion.chat.server.command.vanilla.*;
import infuzion.chat.server.plugin.command.ICommandExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private List<ICommandExecutor> vanillaCommandExecutors = new ArrayList<>();
    private Map<Command, ICommandExecutor> pluginCommandExecutors = new HashMap<>();

    public CommandManager(Server server) {
        addCommandExecutor(new ChatRoomCommand());
        addCommandExecutor(new ModeratorCommand());
        addCommandExecutor(new ReloadCommand(server));
        addCommandExecutor(new StopCommand(server));
        addCommandExecutor(new TpsCommand(server));
    }

    public void registerCommand(Command command, ICommandExecutor executor) {
        pluginCommandExecutors.put(command, executor);
    }

    public List<ICommandExecutor> getCommandExecutors() {
        return vanillaCommandExecutors;
    }

    private void addCommandExecutor(ICommandExecutor executor) {
        vanillaCommandExecutors.add(executor);
    }

    public void executeCommand(String command, String[] args, IChatClient client) {
        for (ICommandExecutor executor : vanillaCommandExecutors) {
            executor.onCommand(command, args, client);
        }
        for (Map.Entry<Command, ICommandExecutor> e : pluginCommandExecutors.entrySet()) {
            if (e.getKey().equals(new Command(command))) {
                e.getValue().onCommand(command, args, client);
            }
        }
    }
}
