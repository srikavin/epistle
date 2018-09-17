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

package me.infuzion.chat.server.api.permission;/*
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

import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.command.Command;

public interface IPermissionManager {
    /**
     * Checks if the specified chat client has the specified permission
     *
     * @param permission Permission as a string
     * @param chatClient ChatClient to check
     *
     * @return A boolean indicating if the client has the specified permission
     */
    boolean hasPermission(String permission, IChatClient chatClient);

    /**
     * Checks if the specified chat client has permission to execute the given command
     *
     * @param command    Command
     * @param chatClient ChatClient to check
     *
     * @return A boolean indicating if the client has the specified permission
     */
    boolean hasPermission(Command command, IChatClient chatClient);

    /**
     * Checks if the specified chat client has the specified permission
     *
     * @param permission Permission
     * @param chatClient ChatClient to check
     *
     * @return A boolean indicating if the client has the specified permission
     */
    boolean hasPermission(Permission permission, IChatClient chatClient);

    /**
     * Registers a permission and a command
     * If a client does not have the permission when executing the command, the command will not be executed
     *
     * @param command    The command to register
     * @param permission The permission to register
     */
    void registerPermission(Command command, Permission permission);

    /**
     * Registers a permission and a command
     * If a client does not have the permission when executing the command, the command will not be executed
     *
     * @param command    The command to register
     * @param permission The permission to register
     */
    void registerPermission(String command, String permission);

    /**
     * Gets the permission attachment of a client
     *
     * @param chatClient Chat client to get the permission attachment of
     *
     * @return The permission attachment of the specified chat client
     *
     * @implNote This should update the permission attachment of the chatclient as well as returning it
     */
    PermissionAttachment getPermissionAttachment(IChatClient chatClient);

    /**
     * Gets the message to be sent when a client does not have permission
     *
     * @return A string containing the message to be sent when a client does not have permission
     */
    @SuppressWarnings("SameReturnValue")
    default String noPermissionMessage() {
        return "Sorry, you are not allowed to perform that action.";
    }

}
