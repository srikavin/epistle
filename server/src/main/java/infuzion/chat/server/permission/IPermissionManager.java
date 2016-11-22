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

package infuzion.chat.server.permission;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.command.Command;

public interface IPermissionManager {
    default boolean hasPermission(String permission, IChatClient chatClient) {
        return hasPermission(new Permission(permission), chatClient);
    }

    boolean hasPermission(Permission permission, IChatClient chatClient);

    PermissionAttachment getPermissions(IChatClient client);

    void registerPermission(Command command, Permission permission);

    default void registerPermission(String command, String permission) {
        registerPermission(new Command(command), new Permission(permission));
    }

    PermissionAttachment getPermissionAttachment(IChatClient chatClient);

    default String noPermissionMessage() {
        return "Sorry, you are not allowed to perform that action.";
    }

}
