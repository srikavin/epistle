package infuzion.chat.server.command;

import infuzion.chat.server.ChatClient;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private static List<ICommandExecutor> commandExecutors = new ArrayList<>();

    static {
        addCommandExecutor(new ChatRoomCommand());
    }

    public static List<ICommandExecutor> getCommandExecutors() {
        return commandExecutors;
    }

    public static void addCommandExecutor(ICommandExecutor executor) {
        commandExecutors.add(executor);
    }

    public static void executeCommand(String command, String[] args, ChatClient client) {
        for (ICommandExecutor executor : commandExecutors) {
            executor.onCommand(command, args, client);
        }
    }
}
