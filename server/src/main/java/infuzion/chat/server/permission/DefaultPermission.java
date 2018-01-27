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

package infuzion.chat.server.permission;

import me.infuzion.chat.server.api.permission.Permission;
import me.infuzion.chat.server.api.permission.PermissionDefault;

public class DefaultPermission implements Permission {
    private final String permission;
    private final PermissionDefault type;

    public DefaultPermission(String permission) {
        this(permission, PermissionDefault.FALSE);
    }

    public DefaultPermission(String permission, PermissionDefault defaultType) {
        this.permission = permission;
        this.type = defaultType;
    }

    @Override
    public String getName() {
        return permission;
    }

    @Override
    public PermissionDefault getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DefaultPermission
                && o.toString().equals(this.getName())
                && type.equals(((Permission) o).getType());
    }
}
