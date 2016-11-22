/*
 *
 *  *  Copyright 2016 Infuzion
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package infuzion.chat.server.command;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.Server;
import infuzion.chat.server.command.vanilla.*;
import infuzion.chat.server.event.IEventManager;
import infuzion.chat.server.event.command.PreCommandEvent;
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
        addCommandExecutor(new ModeratorCommand(server));
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

    public void executeCommand(String command, String[] args, IChatClient client, IEventManager eventManager) {
        PreCommandEvent event = new PreCommandEvent(command, args, client);
        eventManager.fireEvent(event);
        if (event.isCanceled()) {
            return;
        }
        System.out.println(client.getName() + " executed: " + command);
        for (ICommandExecutor executor : vanillaCommandExecutors) {
            executor.onCommand(command, args, client);
        }
        pluginCommandExecutors.entrySet().stream().filter(e -> e.getKey().equals(new Command(command))).forEach(e -> e.getValue().onCommand(command, args, client));
    }
}
