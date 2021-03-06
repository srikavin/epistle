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

import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.Server;
import me.infuzion.chat.server.api.event.command.PreCommandEvent;
import me.infuzion.chat.server.api.permission.DefaultPermission;
import me.infuzion.chat.server.api.permission.IPermissionGroup;
import me.infuzion.chat.server.api.permission.PermissionAttachment;
import me.infuzion.chat.server.mock.FakeClient;
import me.infuzion.chat.server.mock.FakeServer;
import me.infuzion.chat.server.mock.FakeServerClient;
import me.infuzion.chat.server.permission.DefaultPermissionAttachment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DefaultPermissionManagerTest {
    @Mock
    Server server;

    @Test
    public void hasPermission() {
        Map<String, Map<String, List<String>>> map = new HashMap<>();

        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server, map);
        IChatClient fakeClient = new FakeClient();

        assertFalse(defaultPermissionManager.hasPermission("should.not.have.this.permission", fakeClient));

        PermissionAttachment pA = new DefaultPermissionAttachment();
        pA.addPermission(new DefaultPermission("should.have.this.permission"));
        fakeClient.setPermissionAttachment(pA);

        assertTrue(defaultPermissionManager.hasPermission(new DefaultPermission("should.have.this.permission"),
                fakeClient));

        IChatClient fakeServerClient = new FakeServerClient();
        assertTrue(defaultPermissionManager.hasPermission("should.have.this", fakeServerClient));

    }

    @Test
    public void hasPermissionGroupTest() {
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
        DefaultPermissionManager dPM = new DefaultPermissionManager(server, map);
        assertTrue(dPM.hasPermission("should.have", fakeClient), "Should have this permission");
        assertTrue(dPM.hasPermission("should.have.from.group", fakeClient),
                "Should have this permission from group");
        assertFalse(dPM.hasPermission("should.not.have", fakeClient), "Should not have this permission");
    }

    @Test
    public void hasPermissionGroupInheritanceTest() {
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

        DefaultPermissionManager dPM = new DefaultPermissionManager(server, map);

        assertTrue(dPM.hasPermission("directly.attached",
                fakeClient), "Should have this permission");

        assertTrue(dPM.hasPermission("from.group.A", fakeClient),
                "Should have this permission from groupA");

        assertTrue(dPM.hasPermission("from.group.B", fakeClient),
                "Should have this permission from groupB");

        assertFalse(dPM.hasPermission("should.not.have",
                fakeClient), "Should not have this permission");
    }

    @Test
    public void registerPermission() {
        Server server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server, new HashMap<>());
        IChatClient fakeClient = new FakeClient();

        defaultPermissionManager.registerPermission("test", "chat.test");

        PreCommandEvent shouldBeCanceled = new PreCommandEvent("test", new String[]{}, fakeClient);
        defaultPermissionManager.onCommand(shouldBeCanceled);

        assertTrue(shouldBeCanceled.isCanceled(), "Event should be canceled");
    }

    @BeforeEach
    public void resetStatic() throws Exception {
        Field field = IPermissionGroup.class.getDeclaredField("groups");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, new ArrayList<DefaultPermissionGroup>());
    }

    @Test
    public void getPermissionAttachment() {
        Server server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server, new HashMap<>());
        IChatClient fakeClient = new FakeClient();

        PermissionAttachment shouldBeEmpty = defaultPermissionManager.getPermissionAttachment(fakeClient);
        assertEquals(0, shouldBeEmpty.getPermissions().size(), "Should be empty");
    }

}