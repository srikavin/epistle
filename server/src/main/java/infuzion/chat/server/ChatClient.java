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

package infuzion.chat.server;

import infuzion.chat.common.DataType;
import infuzion.chat.server.permission.PermissionAttachment;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class ChatClient implements IChatClient {
    private final static Map<Socket, IChatClient> clientSocketMap = new HashMap<>();
    private final static Map<String, IChatClient> clientStringMap = new HashMap<>();
    private final String name;
    private final UUID uuid;
    private final Socket socket;
    private PermissionAttachment permissionAttachment;
    private String prefix;
    private DataOutputStream outputStream;
    private IChatRoom currentIChatRoom;

    public ChatClient(String name, UUID uuid, Socket socket) {
        this.name = name;
        this.uuid = uuid;
        try {
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = socket;
        clientSocketMap.put(socket, this);
        clientStringMap.put(name, this);
        prefix = "[" + name + "] ";
    }

    public static IChatClient fromSocket(Socket sock) {
        return clientSocketMap.get(sock);
    }

    public static IChatClient fromName(String name) {
        return clientStringMap.get(name);
    }

    public void kick(String message) {
        sendMessage(message);
        clientSocketMap.remove(socket);
        clientStringMap.remove(name);
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
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
        sendData(message, DataType.Message);
    }

    public IChatRoom getChatRoom() {
        return currentIChatRoom;
    }

    public void setChatRoom(IChatRoom IChatRoom) {
        this.currentIChatRoom = IChatRoom;
    }

    @SuppressWarnings("Duplicates")
    public void sendData(String data, DataType id) {
        try {
            outputStream.writeByte(id.byteValue);
            outputStream.writeUTF(data);
            outputStream.writeByte(DataType.EndOfData.byteValue);
        } catch (IOException e) {
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IChatClient && this.uuid.equals(((IChatClient) obj).getUuid());
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
}
