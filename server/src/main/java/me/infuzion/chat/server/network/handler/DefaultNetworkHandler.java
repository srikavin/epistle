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

package me.infuzion.chat.server.network.handler;

import infuzion.chat.common.DataType;
import me.infuzion.chat.server.ChatClient;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoomManager;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;
import me.infuzion.chat.server.api.event.chat.MessageEvent;
import me.infuzion.chat.server.api.event.connection.JoinEvent;
import me.infuzion.chat.server.api.network.ClientConnection;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class DefaultNetworkHandler extends NetworkHandler {
    private final IEventManager eventManager;
    private final IChatRoomManager chatRoomManager;
    private final ICommandManager commandManager;

    public DefaultNetworkHandler(IEventManager eventManager, IChatRoomManager chatRoomManager, ICommandManager commandManager) {
        this.eventManager = eventManager;
        this.chatRoomManager = chatRoomManager;
        this.commandManager = commandManager;
    }

    @Override
    public void handleKeepAlive(IChatClient client, ByteBuffer buffer) {

    }

    @Override
    public void handleMessage(IChatClient client, ByteBuffer buffer) {
        int size = buffer.getInt();
        String message = readString(size, buffer);
        MessageEvent event = new MessageEvent(client, message);
        eventManager.fireEvent(event);

        if (!event.isCanceled()) {
            chatRoomManager.sendMessageAll(message, client, client.getChatRoom());
        }
    }

    @Override
    public void handleCommand(IChatClient client, ByteBuffer buffer) {
        int size = buffer.getInt();

        String message = readString(size, buffer);

        String[] split = message.split(" ");
        String command;
        String[] args;
        if (split.length > 1) {
            command = split[0].replace("/", "");
            args = Arrays.copyOfRange(split, 1, split.length);
        } else {
            command = message.replace("/", "");
            args = new String[]{};
        }
        commandManager.executeCommand(command, args, client, eventManager);
    }

    @Override
    public void handleColorChange(IChatClient client, ByteBuffer buffer) {

    }

    @Override
    public void handleKick(IChatClient client, ByteBuffer buffer) {
        System.out.println("Client tried to kick us");
        client.kick("for trying to kick the server");
    }

    @Override
    public IChatClient handleHello(ClientConnection connection, ByteBuffer buffer) {
        byte type = buffer.get();

        if (type != DataType.ClientHello.byteValue) {
            throw new RuntimeException("Invalid packet signature");
        }

        int nameSize = buffer.getInt();

        System.out.println("nameSize = " + nameSize);

        byte[] nameBytes = new byte[nameSize];
        buffer.get(nameBytes);

        String name = new String(nameBytes, StandardCharsets.UTF_8);

        IChatClient client = new ChatClient(name, UUID.nameUUIDFromBytes(name.getBytes()), connection);

        JoinEvent joinEvent = new JoinEvent(client);
        eventManager.fireEvent(joinEvent);

        if (joinEvent.isCanceled()) {
            chatRoomManager.kickClient(client, "banned");
        }

        chatRoomManager.addClient(client);

        System.out.println("Client connected: ");
        System.out.println("UUID: " + client.getUuid());
        System.out.println("Name: " + client.getName());
        System.out.println();

        return client;
    }
}
