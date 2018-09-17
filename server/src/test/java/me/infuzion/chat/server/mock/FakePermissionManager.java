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
import me.infuzion.chat.server.api.permission.DefaultPermission;
import me.infuzion.chat.server.api.permission.IPermissionManager;
import me.infuzion.chat.server.api.permission.Permission;
import me.infuzion.chat.server.api.permission.PermissionAttachment;

public class FakePermissionManager implements IPermissionManager {
    @Override
    public boolean hasPermission(String permission, IChatClient chatClient) {
        return hasPermission(new DefaultPermission(permission), chatClient);
    }

    @Override
    public boolean hasPermission(Command permission, IChatClient chatClient) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission permission, IChatClient chatClient) {
        return false;
    }

    @Override
    public void registerPermission(Command command, Permission permission) {

    }

    @Override
    public void registerPermission(String command, String permission) {

    }

    @Override
    public PermissionAttachment getPermissionAttachment(IChatClient chatClient) {
        return null;
    }
}
