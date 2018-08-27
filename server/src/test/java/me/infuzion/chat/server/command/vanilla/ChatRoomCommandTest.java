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

package me.infuzion.chat.server.command.vanilla;

import me.infuzion.chat.server.ChatRoomManager;
import me.infuzion.chat.server.mock.FakeClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatRoomCommandTest {

    @Test
    public void onCommand() {
        ChatRoomManager chatRoomManager = new ChatRoomManager();
        ChatRoomCommand chatRoomCommand = new ChatRoomCommand();
        FakeClient client = new FakeClient();
        chatRoomCommand.onCommand("chatroom", new String[]{}, client);
        //Default chatroom
        assertEquals(1, chatRoomManager.getChatRooms().size());
        assertTrue(client.sendMessageCalled);
        client.reset();


        chatRoomCommand.onCommand("chatroom", new String[]{"create", null}, client);
        //Default chatroom
        assertTrue(client.sendMessageCalled);
        client.reset();

        chatRoomCommand.onCommand("chatroom", new String[]{"create"}, client);
        assertEquals(1, chatRoomManager.getChatRooms().size());

        chatRoomCommand.onCommand("chatroom", new String[]{"create", "testRoom"}, client);
        assertNotNull(chatRoomManager.fromString("testRoom"));
        assertEquals(2, chatRoomManager.getChatRooms().size());

        chatRoomCommand.onCommand("chatroom", new String[]{"move"}, client);
        assertNull(client.getChatRoom());
        assertTrue(client.sendMessageCalled);
        client.reset();

        chatRoomCommand.onCommand("chatroom", new String[]{"move", null}, client);
        assertNull(client.getChatRoom());
        assertTrue(client.sendMessageCalled);
        client.reset();

        chatRoomCommand.onCommand("chatroom", new String[]{"move", "testRoom"}, client);
        assertNotNull(client.getChatRoom());
        assertEquals("testRoom", client.getChatRoom().getName());

    }
}