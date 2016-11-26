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

package infuzion.chat.server.permission.def;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.event.command.PreCommandEvent;
import infuzion.chat.server.mock.FakeClient;
import infuzion.chat.server.mock.FakeServer;
import infuzion.chat.server.mock.FakeServerClient;
import infuzion.chat.server.permission.IPermissionGroup;
import infuzion.chat.server.permission.Permission;
import infuzion.chat.server.permission.PermissionAttachment;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultPermissionManagerTest {
    @Test
    public void hasPermission() throws Exception {
        IServer server = new FakeServer();

        Map<String, Map<String, List<String>>> map = new HashMap<>();

        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server, map);
        IChatClient fakeClient = new FakeClient();

        assertFalse(defaultPermissionManager.hasPermission("should.not.have.this.permission", fakeClient));

        PermissionAttachment pA = new PermissionAttachment();
        pA.addPermission(new Permission("should.have.this.permission"));
        fakeClient.setPermissionAttachment(pA);

        assertTrue(defaultPermissionManager.hasPermission(new Permission("should.have.this.permission"),
                fakeClient));

        IChatClient fakeServerClient = new FakeServerClient();
        assertTrue(defaultPermissionManager.hasPermission("should.have.this", fakeServerClient));

    }

    @Test
    public void hasPermissionGroupTest() throws Exception {
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        IChatClient fakeClient = new FakeClient();

        Map<String, List<String>> map1 = new HashMap<>();
        List<String> permissions = new ArrayList<>();
        List<String> groups = new ArrayList<>();

        permissions.add("should.have");
        groups.add("testGroup");

        map1.put("permissions", permissions);
        map1.put("group", groups);
        map.put(fakeClient.getUuid().toString(), map1);

        permissions = new ArrayList<>();
        groups = new ArrayList<>();
        map1 = new HashMap<>();

        permissions.add("should.have.from.group");
        map1.put("permissions", permissions);
        map1.put("group", groups);
        map.put("testGroup", map1);
        DefaultPermissionManager dPM = new DefaultPermissionManager(new FakeServer(), map);
        assertTrue("Should have this permission", dPM.hasPermission("should.have", fakeClient));
        assertTrue("Should have this permission from group",
                dPM.hasPermission("should.have.from.group", fakeClient));
        assertFalse("Should not have this permission", dPM.hasPermission("should.not.have",
                fakeClient));
    }

    @Test
    public void hasPermissionGroupInheritanceTest() throws Exception {
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        IChatClient fakeClient = new FakeClient();

        Map<String, List<String>> map1 = new HashMap<>();
        List<String> permissions = new ArrayList<>();
        List<String> groups = new ArrayList<>();

        //Direct Permissions
        permissions.add("directly.attached");
        groups.add("groupA");
        map1.put("permissions", permissions);
        map1.put("group", groups);

        map.put(fakeClient.getUuid().toString(), map1);

        //Group A Permissions
        permissions = new ArrayList<>();
        groups = new ArrayList<>();
        map1 = new HashMap<>();

        groups.add("groupB");
        permissions.add("from.group.A");
        map1.put("permissions", permissions);
        map1.put("group", groups);
        map.put("groupA", map1);

        //Group B Permissions
        permissions = new ArrayList<>();
        groups = new ArrayList<>();
        map1 = new HashMap<>();

        permissions.add("from.group.B");

        map1.put("permissions", permissions);
        map1.put("group", groups);
        map.put("groupB", map1);

        DefaultPermissionManager dPM = new DefaultPermissionManager(new FakeServer(), map);

        assertTrue("Should have this permission", dPM.hasPermission("directly.attached",
                fakeClient));

        assertTrue("Should have this permission from groupA",
                dPM.hasPermission("from.group.A", fakeClient));

        assertTrue("Should have this permission from groupB",
                dPM.hasPermission("from.group.B", fakeClient));

        assertFalse("Should not have this permission", dPM.hasPermission("should.not.have",
                fakeClient));
    }

    @Test
    public void registerPermission() throws Exception {
        IServer server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server, new HashMap<>());
        IChatClient fakeClient = new FakeClient();

        defaultPermissionManager.registerPermission("test", "chat.test");

        PreCommandEvent shouldBeCanceled = new PreCommandEvent("test", new String[]{}, fakeClient);
        defaultPermissionManager.onCommand(shouldBeCanceled);

        assertTrue("Event should be canceled", shouldBeCanceled.isCanceled());
    }

    @Before
    public void resetStatic() throws Exception {
        Field field = IPermissionGroup.class.getDeclaredField("groups");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, new ArrayList<DefaultPermissionGroup>());
    }

    @Test
    public void getPermissionAttachment() throws Exception {
        IServer server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server, new HashMap<>());
        IChatClient fakeClient = new FakeClient();

        PermissionAttachment shouldBeEmpty = defaultPermissionManager.getPermissionAttachment(fakeClient);
        assertEquals("Should be empty", 0, shouldBeEmpty.getPermissions().size());
    }

}