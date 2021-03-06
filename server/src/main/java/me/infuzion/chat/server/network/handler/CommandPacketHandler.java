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

package me.infuzion.chat.server.network.handler;

import infuzion.chat.common.network.packet.CommandPacket;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;

import java.util.Arrays;

public class CommandPacketHandler {
    private final ICommandManager commandManager;
    private final IEventManager eventManager;

    public CommandPacketHandler(ICommandManager commandManager, IEventManager eventManager) {
        this.commandManager = commandManager;
        this.eventManager = eventManager;
    }

    public void handle(CommandPacket packet, IChatClient client) {
        String message = packet.getCommand();

        String[] split = message.split(" ");
        String command;
        String[] args;
        if (split.length > 1) {
            command = split[0].replace("/", "");
            args = Arrays.copyOfRange(split, 1, split.length);
        } else {
            command = message.replace("/", "");
            args = new String[0];
        }
        commandManager.executeCommand(command, args, client, eventManager);
    }
}
