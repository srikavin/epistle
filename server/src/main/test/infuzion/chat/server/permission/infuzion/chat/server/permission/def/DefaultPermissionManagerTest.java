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

package infuzion.chat.server.permission.infuzion.chat.server.permission.def;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.event.command.PreCommandEvent;
import infuzion.chat.server.permission.FakeClient;
import infuzion.chat.server.permission.FakeServer;
import infuzion.chat.server.permission.Permission;
import infuzion.chat.server.permission.PermissionAttachment;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultPermissionManagerTest {
    @Test
    public void hasPermission() throws Exception {
        IServer server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server);
        IChatClient fakeClient = new FakeClient();
        assertFalse(defaultPermissionManager.hasPermission("should.not.have.this.permission", fakeClient));
        PermissionAttachment pA = new PermissionAttachment();
        pA.addPermission(new Permission("should.have.this.permission"));
        fakeClient.setPermissionAttachment(pA);
        assertTrue(defaultPermissionManager.hasPermission(new Permission("should.have.this.permission"), fakeClient));
    }

    @Test
    public void registerPermission() throws Exception {
        IServer server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server);
        IChatClient fakeClient = new FakeClient();
        defaultPermissionManager.registerPermission("test", "chat.test");
        PreCommandEvent shouldBeCanceled = new PreCommandEvent("test", new String[]{}, fakeClient);
        defaultPermissionManager.onCommand(shouldBeCanceled);
        assertTrue("Event should be canceled", shouldBeCanceled.isCanceled());
    }

    @Test
    public void getPermissionAttachment() throws Exception {
        IServer server = new FakeServer();
        DefaultPermissionManager defaultPermissionManager = new DefaultPermissionManager(server);
        IChatClient fakeClient = new FakeClient();
        PermissionAttachment shouldBeEmpty = defaultPermissionManager.getPermissionAttachment(fakeClient);
        assertTrue("Should be empty", shouldBeEmpty.getPermissions().size() == 0);
    }

}