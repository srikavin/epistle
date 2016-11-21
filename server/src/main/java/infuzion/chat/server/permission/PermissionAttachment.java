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

package infuzion.chat.server.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents permissions attached to a {@link infuzion.chat.server.IChatClient ChatClient}
 */
public class PermissionAttachment implements Iterable {
    private List<Permission> permissions;

    /**
     * Creates this object with no permissions
     */
    public PermissionAttachment() {
        this(new ArrayList<>());
    }

    /**
     * Creates this object with the specified list of permissions
     *
     * @param permissions List of permissions to set
     */
    public PermissionAttachment(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @param permission Permission to check for
     * @return Whether or not the {@link infuzion.chat.server.IChatClient ChatClient} contains the specified permission
     */
    public boolean containsPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator iterator() {
        return new Iterator() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return permissions.size() < index;
            }

            @Override
            public Object next() {
                index++;
                return permissions.get(index - 1);
            }
        };
    }
}
