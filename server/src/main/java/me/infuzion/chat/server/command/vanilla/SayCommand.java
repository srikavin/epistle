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

package me.infuzion.chat.server.command.vanilla;

import me.infuzion.chat.server.ChatRoom;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.DefaultCommand;

public class SayCommand implements VanillaCommandExecutor {
    @Override
    public Command[] getCommands() {
        return new Command[]{
                new DefaultCommand("say")
        };
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equals("say")) {
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(" ").append(arg);
            }

            ChatRoom.getChatRoomManager().sendMessageAll(message.toString(), client);
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }
}
