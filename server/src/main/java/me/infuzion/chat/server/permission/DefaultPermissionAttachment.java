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

package me.infuzion.chat.server.permission;

import me.infuzion.chat.server.api.permission.Permission;
import me.infuzion.chat.server.api.permission.PermissionAttachment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Represents permissions attached to a {@link me.infuzion.chat.server.api.IChatClient ChatClient}
 */
public class DefaultPermissionAttachment implements PermissionAttachment {
    private final List<Permission> permissions;

    /**
     * Creates this object with no permissions
     */
    public DefaultPermissionAttachment() {
        this(new ArrayList<>());
    }

    /**
     * Creates this object with the specified list of permissions
     *
     * @param permissions List of permissions to set
     */
    public DefaultPermissionAttachment(List<Permission> permissions) {
        if (permissions != null) {
            this.permissions = permissions;
        } else {
            this.permissions = new ArrayList<>();
        }
    }

    @Override
    public void addPermission(Permission permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    @Override
    public void addPermissions(PermissionAttachment permissions) {
        for (Permission e : permissions) {
            addPermission(e);
        }
    }

    @Override
    public List<Permission> getPermissions() {
        return new ArrayList<>(permissions);
    }

    @Override
    public boolean containsPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * Returns an iterator over elements of type {@link Permission}
     *
     * @return an {@link Iterator} that will iterate through all permissions contained in this object.
     */
    @Override
    public Iterator<Permission> iterator() {
        return new Iterator<Permission>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return permissions.size() > index;
            }

            @Override
            public Permission next() {
                index++;
                return permissions.get(index - 1);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DefaultPermissionAttachment) {
            DefaultPermissionAttachment e = (DefaultPermissionAttachment) o;
            e.permissions.sort(Comparator.comparing(Permission::getName));
            this.permissions.sort(Comparator.comparing(Permission::getName));
            return permissions.equals(e.getPermissions());
        }
        return false;
    }
}
