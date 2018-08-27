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
import me.infuzion.chat.server.api.permission.PermissionAttachment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionAttachmentTest {
    @Test
    public void addPermission() {
        PermissionAttachment pA = new DefaultPermissionAttachment();

        DefaultPermission permission1 = new DefaultPermission("1");
        pA.addPermission(permission1);
        assertTrue(pA.getPermissions().contains(permission1));
    }

    @Test
    public void nonNullList() {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new DefaultPermission("1"));
        permissions.add(new DefaultPermission("2"));
        permissions.add(new DefaultPermission("3"));
        permissions.add(new DefaultPermission("4"));
        PermissionAttachment pA = new DefaultPermissionAttachment(permissions);
        assertTrue(pA.containsPermission(new DefaultPermission("1")));
        assertTrue(pA.containsPermission(new DefaultPermission("2")));
        assertTrue(pA.containsPermission(new DefaultPermission("3")));
        assertTrue(pA.containsPermission(new DefaultPermission("4")));
    }

    @Test
    public void equals() {
        PermissionAttachment pA = new DefaultPermissionAttachment();
        pA.addPermission(new DefaultPermission("213"));
        Object obj = new Object();
        assertNotEquals(pA, obj);

        PermissionAttachment pA1 = new DefaultPermissionAttachment();
        pA1.addPermission(new DefaultPermission("123"));

        assertNotEquals(pA, pA1);
        assertNotEquals(pA1, pA);

        pA.addPermission(new DefaultPermission("123"));
        pA1.addPermission(new DefaultPermission("213"));

        assertEquals(pA, pA1);
        assertEquals(pA1, pA);
    }

    @Test
    public void addPermissions() {
        DefaultPermission[] permissions = new DefaultPermission[5];
        permissions[0] = new DefaultPermission("1");
        permissions[1] = new DefaultPermission("2");
        permissions[2] = new DefaultPermission("3");
        permissions[3] = new DefaultPermission("4");
        permissions[4] = new DefaultPermission("5");

        List<Permission> permissionList1 = new ArrayList<>();
        permissionList1.add(permissions[0]);
        permissionList1.add(permissions[1]);
        permissionList1.add(permissions[2]);
        permissionList1.add(permissions[3]);
        PermissionAttachment check1 = new DefaultPermissionAttachment(permissionList1);

        PermissionAttachment pA1 = new DefaultPermissionAttachment();
        pA1.addPermission(permissions[0]);
        pA1.addPermission(permissions[1]);
        PermissionAttachment pA2 = new DefaultPermissionAttachment();
        pA2.addPermission(permissions[2]);
        pA2.addPermission(permissions[3]);
        pA1.addPermissions(pA2);

        assertEquals(check1.getPermissions(), pA1.getPermissions());
    }

    @Test
    public void getPermissions() {
        PermissionAttachment pA = new DefaultPermissionAttachment();
        assertEquals(0, pA.getPermissions().size());
        pA.addPermission(new DefaultPermission("123"));
        assertEquals(1, pA.getPermissions().size());
        for (int x = 0; x < 100; x++) {
            pA.addPermission(new DefaultPermission("permission." + x));
        }
        assertEquals(101, pA.getPermissions().size());
    }

    @Test
    public void containsPermission() {
        PermissionAttachment pA = new DefaultPermissionAttachment();
        pA.addPermission(new DefaultPermission("123"));
        assertTrue(pA.containsPermission(new DefaultPermission("123")));
    }

    @Test
    public void iterator() {
        PermissionAttachment pA = new DefaultPermissionAttachment();
        DefaultPermission[] permissions = new DefaultPermission[5];
        permissions[0] = new DefaultPermission("1");
        permissions[1] = new DefaultPermission("2");
        permissions[2] = new DefaultPermission("3");
        permissions[3] = new DefaultPermission("4");
        permissions[4] = new DefaultPermission("5");
        for (DefaultPermission p : permissions) {
            pA.addPermission(p);
        }
        Iterator iterator = pA.iterator();
        int counter = 0;
        assertTrue(iterator.hasNext());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), permissions[counter]);
            counter++;
        }
        assertEquals(5, counter);
    }

}