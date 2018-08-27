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

import me.infuzion.chat.server.api.permission.DefaultPermission;
import me.infuzion.chat.server.api.permission.Permission;
import me.infuzion.chat.server.api.permission.PermissionDefault;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PermissionTest {
    @Test
    public void toStringTest() {
        DefaultPermission permission = new DefaultPermission("123");
        assertEquals("123", permission.getName());

        DefaultPermission permission1 = new DefaultPermission("213");
        assertNotEquals(permission.getName(), permission1.getName());
    }

    @Test
    public void getType() {
        Permission permission = new DefaultPermission("");
        assertEquals(permission.getType(), PermissionDefault.FALSE);
        Permission permission1 = new DefaultPermission("", PermissionDefault.TRUE);
        assertEquals(permission1.getType(), PermissionDefault.TRUE);
    }

    @Test
    public void equals() {
        DefaultPermission permission = new DefaultPermission("123");
        Object object = new Object();
        assertNotEquals(permission, object);
        DefaultPermission permission1 = new DefaultPermission("123");
        assertEquals(permission, permission1);
        assertEquals(permission1, permission);
    }
}
