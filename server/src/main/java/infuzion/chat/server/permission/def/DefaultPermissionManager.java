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

package infuzion.chat.server.permission.def;

import infuzion.chat.server.ChatClient;
import infuzion.chat.server.command.Command;
import infuzion.chat.server.permission.IPermissionManager;
import infuzion.chat.server.permission.Permission;
import infuzion.chat.server.permission.PermissionAttachment;
import infuzion.chat.server.permission.PermissionDefault;

import java.util.HashMap;
import java.util.Map;

public class DefaultPermissionManager implements IPermissionManager {
    private Map<ChatClient, PermissionAttachment> permissionMap;
    private Map<Command, Permission> commandPermissionMap;

    public DefaultPermissionManager() {
        this.permissionMap = new HashMap<>();
        this.commandPermissionMap = new HashMap<>();
    }

    @Override
    public boolean hasPermission(Permission permission, ChatClient chatClient) {
        if (permission.getType().equals(PermissionDefault.TRUE)) { // Needs to be changed for negated permissions
            return true;
        } else {
            if (permissionMap.get(chatClient) != null) {
                return permissionMap.get(chatClient).containsPermission(permission);
            }
        }
        return false;
    }

    @Override
    public PermissionAttachment getPermissions(ChatClient client) {
        return permissionMap.get(client);
    }

    @Override
    public void registerPermission(Command command, Permission permission) {
        commandPermissionMap.put(command, permission);
    }

    @Override
    public PermissionAttachment getPermissionAttachment(ChatClient chatClient) {
        //@TODO
        return null;
    }
}
