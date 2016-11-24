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

public class Permission {
    private final String permission;
    private final PermissionDefault type;

    public Permission(String permission) {
        this(permission, PermissionDefault.FALSE);
    }

    public Permission(String permission, PermissionDefault defaultType) {
        this.permission = permission;
        this.type = defaultType;
    }

    @Override
    public String toString() {
        return permission;
    }

    public PermissionDefault getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Permission
                && o.toString().equals(this.toString())
                && type.equals(((Permission) o).getType());
    }
}
