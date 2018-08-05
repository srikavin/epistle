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

package me.infuzion.chat.server.mock;

import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.ICommandExecutor;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;

import java.util.ArrayList;
import java.util.List;

public class FakeCommandManager implements ICommandManager {
    @Override
    public void registerCommand(Command command, ICommandExecutor executor) {

    }

    @Override
    public List<ICommandExecutor> getCommandExecutors() {
        return new ArrayList<>();
    }

    @Override
    public void addCommandExecutor(ICommandExecutor executor) {

    }

    @Override
    public void executeCommand(String command, String[] args, IChatClient client, IEventManager eventManager) {

    }
}
