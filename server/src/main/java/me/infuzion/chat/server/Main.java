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

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ChatServer server;
        int socketPort = 7776;
        int wsPort = 80;
        for (String e : args) {
            if (e.startsWith("-sPort")) {
                socketPort = Integer.parseInt(e.substring("-sPort".length()));
                continue;
            }
            if (e.startsWith("-wsPort")) {
                wsPort = Integer.parseInt(e.substring("-wsPort".length()));
                continue;
            }
            System.out.println("Unknown argument: " + e);
            return;
        }
        server = new ChatServer(socketPort, wsPort);
        server.start();
    }
}
