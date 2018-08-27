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

package me.infuzion.chat.server.mock;

import infuzion.chat.common.DataType;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoom;
import me.infuzion.chat.server.api.network.ClientConnection;
import me.infuzion.chat.server.api.permission.PermissionAttachment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class FakeClient implements IChatClient {
    public boolean sendMessageCalled = false;
    public List<String> receivedMessages = new ArrayList<>();
    private PermissionAttachment permissions;
    private IChatRoom chatRoom;

    @Override
    public void kick(String message) {

    }

    @Override
    public UUID getUuid() {
        return UUID.fromString("d74b0076-1b16-481c-9a7c-f3bd84c659a1");
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void sendMessage(String message) {
        sendMessageCalled = true;
        receivedMessages.add(message);
    }

    public void reset() {
        sendMessageCalled = false;
        receivedMessages.clear();
    }

    @Override
    public IChatRoom getChatRoom() {
        return chatRoom;
    }

    @Override
    public void setChatRoom(IChatRoom IChatRoom) {
        chatRoom = IChatRoom;
    }

    @Override
    public void sendData(String data, DataType id) {

    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void setPrefix(String prefix) {

    }

    @Override
    public PermissionAttachment getPermissionAttachment() {
        return permissions;
    }

    @Override
    public void setPermissionAttachment(PermissionAttachment attachment) {
        this.permissions = attachment;
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public ClientConnection getConnection() {
        return mock(ClientConnection.class);
    }
}
