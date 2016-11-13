/*
 *  Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package infuzion.chat.server.command.vanilla;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.Server;
import infuzion.chat.server.plugin.command.ICommandExecutor;

public class ReloadCommand implements ICommandExecutor {

    private Server server;

    public ReloadCommand(Server server) {
        this.server = server;
    }

    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        if (commandName.equals("reload")) {
            server.reload();
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }
}
