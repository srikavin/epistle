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

package me.infuzion.chat.server.api.permission;

import java.util.List;

public interface PermissionAttachment extends Iterable<Permission> {
    void addPermission(Permission permission);

    /**
     * Adds the specified PermissionAttachment to this PermissionAttachment
     *
     * @param permissions The PermissionAttachment to add
     */
    void addPermissions(PermissionAttachment permissions);

    List<Permission> getPermissions();

    /**
     * @param permission Permission to check for
     *
     * @return Whether or not the {@link me.infuzion.chat.server.api.IChatClient ChatClient} contains the specified permission
     */
    boolean containsPermission(Permission permission);
}
