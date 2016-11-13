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

public interface IChatRoomManager {
    void addChatRoom(IChatRoom IChatRoom);

    void addClient(IChatClient client);

    void addClient(IChatClient client, IChatRoom room);

    void addClient(IChatClient client, String roomName);

    IChatRoom fromString(String string);

    void sendMessageAll(String message, IChatClient client);

    void sendMessageAll(String message, IChatClient client, IChatRoom IChatRoom);

    void moveClient(IChatClient client, IChatRoom newRoom);

    void removeClient(IChatClient IChatClient);

    void kickClient(IChatClient IChatClient, String message);

    void kickClient(IChatClient IChatClient);
}
