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

package infuzion.chat.server.command.vanilla;

import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IServer;
import me.infuzion.chat.server.api.command.ICommandExecutor;
import me.infuzion.chat.server.api.permission.IPermissionManager;

public class ReloadCommand implements ICommandExecutor {

    private final IServer server;

    public ReloadCommand(IServer server) {
        this.server = server;
        IPermissionManager permissionManager = server.getPermissionManager();
        permissionManager.registerPermission("kick", "chat.kick");
        permissionManager.registerPermission("ban", "chat.ban");
        permissionManager.registerPermission("mute", "chat.mute");
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
