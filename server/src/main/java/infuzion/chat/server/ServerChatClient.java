/*
 *  Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.util.UUID;

public class ServerChatClient implements IChatClient {
    private final UUID uuid = new UUID(0, 0);
    private final String name = "Server";
    private String prefix = "[" + name + "]";

    @Override
    public void kick(String message) {
        System.out.print("Somebody attempted to kick you for " + message);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public IChatRoom getChatRoom() {
        return ChatRoom.getChatRoomManager().fromString("default");
    }

    @Override
    public void setChatRoom(IChatRoom IChatRoom) {
        // do nothing
    }

    @Override
    public void sendData(String data, DataType id) {
        System.out.println("You have received data: " + data);
        System.out.println("The data was a " + id.name());
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean isConsole() {
        return true;
    }
}
