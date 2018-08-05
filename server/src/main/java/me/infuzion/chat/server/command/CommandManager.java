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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements ICommandManager {
    private final List<ICommandExecutor> vanillaCommandExecutors = new ArrayList<>();
    private final Map<Command, ICommandExecutor> pluginCommandExecutors = new HashMap<>();

    public CommandManager(Server server) {
        addCommandExecutor(new ChatRoomCommand());
        addCommandExecutor(new ModeratorCommand(server));
        addCommandExecutor(new ReloadCommand(server));
        addCommandExecutor(new StopCommand(server));
        addCommandExecutor(new TpsCommand(server));
        addCommandExecutor(new ClientInfoCommand(server));
    }

    @Override
    public void registerCommand(Command command, ICommandExecutor executor) {
        pluginCommandExecutors.put(command, executor);
    }

    @Override
    public List<ICommandExecutor> getCommandExecutors() {
        return vanillaCommandExecutors;
    }

    @Override
    public void addCommandExecutor(ICommandExecutor executor) {
        vanillaCommandExecutors.add(executor);
    }

    @Override
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
