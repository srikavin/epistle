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

import infuzion.chat.server.permission.def.DefaultPermissionGroup;

import java.util.ArrayList;
import java.util.List;

public interface IPermissionGroup {

    List<DefaultPermissionGroup> groups = new ArrayList<>();

    static DefaultPermissionGroup fromName(String name) {
        for (DefaultPermissionGroup group : groups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    boolean isDefault();

    void setDefault(boolean aDefault);

    List<IPermissionGroup> getInheritingFrom();

    void setInheritingFrom(List<IPermissionGroup> inheritingFrom);

    PermissionAttachment getPermissions();

    void setPermissions(PermissionAttachment permissions);

    PermissionAttachment getCalculatedPermissions();

    String getName();

    void addGroup(IPermissionGroup group);


}
