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

package me.infuzion.chat.server.network;

import me.infuzion.chat.server.ServerChatClient;
import me.infuzion.chat.server.api.network.NetworkSource;

import java.io.IOException;

public class ServerClientHandler implements NetworkSource {
    private final NetworkManager networkManager;
    private ServerChatClient client;
    private volatile boolean running = true;

    public ServerClientHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void init() {
        client = new ServerChatClient();
        Thread thread = new Thread(this::pollInput);
        thread.setName("Console Input Poller");
        thread.start();
    }

    private void pollInput() {
        while (running) {
            try {
                if (client.getConnection().available() > 0) {
                    networkManager.enqueue(client.getConnection().readMessage(), client, getClass());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reload() {
    }

    @Override
    public void stop() throws Exception {
        running = false;
    }
}
