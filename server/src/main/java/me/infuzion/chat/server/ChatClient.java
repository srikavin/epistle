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

package me.infuzion.chat.server;

import infuzion.chat.common.DataType;
import infuzion.chat.common.network.packet.MessagePacket;
import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoom;
import me.infuzion.chat.server.api.network.ClientConnection;
import me.infuzion.chat.server.api.permission.PermissionAttachment;
import me.infuzion.chat.server.network.SocketConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatClient implements IChatClient {
    private final static Map<String, IChatClient> clientStringMap = new HashMap<>();
    private final String name;
    private final UUID uuid;
    private final ClientConnection connection;
    private PermissionAttachment permissionAttachment;
    private String prefix;
    private IChatRoom currentIChatRoom;

    public ChatClient(String name, UUID uuid, ClientConnection connection) {
        this.name = name;
        this.uuid = uuid;
        this.connection = connection;
        clientStringMap.put(name, this);
        prefix = "[" + name + "] ";
    }

    public static IChatClient fromName(String name) {
        return clientStringMap.get(name);
    }

    public void kick(String message) {
        sendData(message, DataType.Kick);
        clientStringMap.remove(name);
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String message) {
        sendPacket(new MessagePacket(message));
    }

    public IChatRoom getChatRoom() {
        return currentIChatRoom;
    }

    public void setChatRoom(IChatRoom IChatRoom) {
        this.currentIChatRoom = IChatRoom;
    }

    @Deprecated
    public void sendData(String data, DataType id) {
        try {
            connection.sendMessage(id, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(NetworkPacket packet) {
        try {
            System.out.println(((SocketConnection) connection).getSocket().getInetAddress().toString());
            connection.sendPacket(packet);
        } catch (IOException e) {
            ChatRoom.getChatRoomManager().removeClient(this);
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public PermissionAttachment getPermissionAttachment() {
        return permissionAttachment;
    }

    public void setPermissionAttachment(PermissionAttachment permissionAttachment) {
        this.permissionAttachment = permissionAttachment;
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public ClientConnection getConnection() {
        return connection;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IChatClient && this.uuid.equals(((IChatClient) obj).getUuid());
    }
}
