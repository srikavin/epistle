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

package infuzion.chat.server.mock;

import infuzion.chat.common.DataType;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.IChatRoom;

import java.util.List;

public class FakeChatRoom implements IChatRoom {

    public FakeChatRoom(String name) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addClient(IChatClient client) {

    }

    @Override
    public void removeClient(IChatClient client) {

    }

    @Override
    public List<IChatClient> getClients() {
        return null;
    }

    @Override
    public void sendMessage(String message, IChatClient chatClient) {

    }

    @Override
    public void sendData(String data, DataType dataType) {

    }
}
