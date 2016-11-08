package infuzion.chat.server.command;

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.plugin.ICommandExecutor;
import infuzion.chat.server.plugin.vanilla.ChatRoomCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {
    private static List<ICommandExecutor> vanillaCommandExecutors = new ArrayList<>();
    private static HashMap<Command, ICommandExecutor> pluginCommandExecutors = new HashMap<>();

    public CommandManager() {
        addCommandExecutor(new ChatRoomCommand());
    }

    public static void registerCommand(Command command, ICommandExecutor executor) {
        pluginCommandExecutors.put(command, executor);
    }

    public List<ICommandExecutor> getCommandExecutors() {
        return vanillaCommandExecutors;
    }

    public void addCommandExecutor(ICommandExecutor executor) {
        vanillaCommandExecutors.add(executor);
    }

    public void executeCommand(String command, String[] args, ChatClient client) {
        for (ICommandExecutor executor : vanillaCommandExecutors) {
            executor.onCommand(command, args, client);
        }
    }
}
