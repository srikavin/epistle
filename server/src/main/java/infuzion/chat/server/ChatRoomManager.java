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

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ChatRoomManager implements IChatRoomManager {
    private final List<IChatRoom> IChatRooms = new ArrayList<>();

    public ChatRoomManager() {
        IChatRooms.add(new ChatRoom("default"));
        ChatRoom.setChatRoomManager(this);
    }

    @Override
    public void addChatRoom(IChatRoom IChatRoom) {
        IChatRooms.add(IChatRoom);
    }

    @Override
    public void addClient(IChatClient client) {
        addClient(client, IChatRooms.get(0));

    }

    @Override
    public void addClient(IChatClient client, IChatRoom room) {
        room.addClient(client);
        client.setChatRoom(room);
        if (!IChatRooms.contains(room)) {
            IChatRooms.add(room);
        }
    }

    @Override
    public void addClient(IChatClient client, String roomName) {
        IChatRoom IChatRoom = fromString(roomName);
        if (IChatRoom != null) {
            addClient(client, IChatRoom);
        }
    }

    @Override
    public IChatRoom fromString(String string) {
        for (IChatRoom e : IChatRooms) {
            if (e.getName().equalsIgnoreCase(string)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void sendMessageAll(String message, IChatClient client) {
        for (IChatRoom e : IChatRooms) {
            sendMessageAll(message, client, e);
        }
    }

    @Override
    public void sendMessageAll(String message, IChatClient client, IChatRoom IChatRoom) {
        IChatRoom.sendMessage(message, client);
    }

    @Override
    public void moveClient(IChatClient client, IChatRoom newRoom) {
        removeClient(client);
        addClient(client, newRoom);
    }

    @Override
    public void removeClient(IChatClient IChatClient) {
        IChatRooms.stream().filter(e -> e.getClients().contains(IChatClient)).forEach(e -> e.removeClient(IChatClient));
    }

    @Override
    public void kickClient(IChatClient IChatClient, String message) {
        removeClient(IChatClient);
        IChatClient.kick(message);
    }

    @Override
    public void kickClient(IChatClient IChatClient) {
        kickClient(IChatClient, "an unspecified reason.");
    }

    @Override
    public IChatRoom createChatRoom(String name) {
        ChatRoom r = new ChatRoom(name);
        addChatRoom(r);
        return r;
    }

    @Override
    public List<IChatRoom> getChatRooms() {
        return IChatRooms;
    }
}
