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

import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IServer;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.DefaultCommand;

public class TpsCommand implements VanillaCommandExecutor {

    private final static String tpsTemplate = "The server is running at %d ticks per second.";
    private final static String totalTpsTemplate = "The server has run for a total of %d ticks.";
    private final IServer server;

    public TpsCommand(IServer server) {
        this.server = server;
        server.getPermissionManager().registerPermission("tps", "server.tps");
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("tps")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
                for (String e : getHelp()) {
                    client.sendMessage(e);
                }
            } else {
                client.sendMessage(String.format(tpsTemplate, server.getTps()));
                client.sendMessage(String.format(totalTpsTemplate, server.getTotalTps()));
            }
        }
    }

    @Override
    public String[] getHelp() {
        return new String[]{"Usage: /tps"};
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new DefaultCommand("tps")
        };
    }
}
