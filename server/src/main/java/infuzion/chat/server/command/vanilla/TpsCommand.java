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

package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class TpsCommand implements ICommandExecutor {

    private IServer server;

    public TpsCommand(IServer server) {
        this.server = server;
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equalsIgnoreCase("tps")) {
            client.sendMessage("The server is running at " + server.getTps() + " ticks per second.");
            client.sendMessage("The server has run for a total of " + server.getTotalTps() + " ticks.");
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }
}