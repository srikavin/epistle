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

package me.infuzion.chat.server.permission.def;

import me.infuzion.chat.server.api.permission.IPermissionGroup;
import me.infuzion.chat.server.api.permission.PermissionAttachment;
import me.infuzion.chat.server.permission.DefaultPermissionAttachment;

import java.util.ArrayList;
import java.util.List;

public class DefaultPermissionGroup implements IPermissionGroup {

    private final String name;
    private boolean isDefault;
    private List<IPermissionGroup> inheritingFrom;
    private PermissionAttachment permissions;

    public DefaultPermissionGroup(String name, PermissionAttachment permissions, boolean isDefault) {
        this(name, permissions, isDefault, new ArrayList<>());
    }

    public DefaultPermissionGroup(String name, PermissionAttachment permissions, boolean isDefault,
                                  List<IPermissionGroup> inheritingFrom) {
        this.isDefault = isDefault;
        this.inheritingFrom = inheritingFrom;
        this.permissions = permissions;
        this.name = name;
        groups.add(this);
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public List<IPermissionGroup> getInheritingFrom() {
        return inheritingFrom;
    }

    @Override
    public void setInheritingFrom(List<IPermissionGroup> inheritingFrom) {
        this.inheritingFrom = inheritingFrom;
    }

    @Override
    public PermissionAttachment getPermissions() {
        return permissions;
    }

    @Override
    public void setPermissions(PermissionAttachment permissions) {
        this.permissions = permissions;
    }

    @Override
    public PermissionAttachment getCalculatedPermissions() {
        PermissionAttachment temp = new DefaultPermissionAttachment();
        for (IPermissionGroup group : inheritingFrom) {
            temp.addPermissions(group.getCalculatedPermissions());
        }
        temp.addPermissions(permissions);
        return temp;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addGroup(IPermissionGroup group) {
        inheritingFrom.add(group);
    }


}
