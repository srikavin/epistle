/*
 * Copyright 2018 Srikavin Ramkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.infuzion.chat.server.command;

import me.infuzion.chat.server.Server;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.DefaultCommand;
import me.infuzion.chat.server.api.command.ICommandExecutor;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;
import me.infuzion.chat.server.api.event.command.PreCommandEvent;
import me.infuzion.chat.server.command.vanilla.*;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements ICommandManager {
    private final Map<Command, ICommandExecutor> pluginCommandExecutors = new HashMap<>();

    public CommandManager(Server server) {
        final VanillaCommandExecutor[] executors = {
                new ChatRoomCommand(),
                new ModeratorCommand(server),
                new ReloadCommand(server),
                new StopCommand(server),
                new TpsCommand(server),
                new ClientInfoCommand(server),
                new SayCommand()
        };

        for (VanillaCommandExecutor e : executors) {
            registerCommands(e, e.getCommands());
        }
    }

    @Override
    public void registerCommand(Command command, ICommandExecutor executor) {
        pluginCommandExecutors.put(command, executor);
    }

    @Override
    public void registerCommands(ICommandExecutor executor, Command... commands) {
        if (commands.length == 0) {
            throw new RuntimeException("Expected at least 1 command to be registered to this executor");
        }
        for (Command e : commands) {
            pluginCommandExecutors.put(e, executor);
        }
    }

    @Override
    public void executeCommand(String command, String[] args, IChatClient client, IEventManager eventManager) {
        PreCommandEvent event = new PreCommandEvent(command, args, client);
        eventManager.fireEvent(event);
        if (event.isCanceled()) {
            return;
        }
        System.out.println(client.getName() + " executed: " + command);

        boolean found = false;
        for (Map.Entry<Command, ICommandExecutor> e : pluginCommandExecutors.entrySet()) {
            if (e.getKey().equals(new DefaultCommand(command))) {
                e.getValue().onCommand(command, args, client);
                found = true;
            }
        }
        if (!found) {
            client.sendMessage("Could not find command /" + command);
        }
    }
}
