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

import me.infuzion.chat.server.mock.FakeClient;
import me.infuzion.chat.server.mock.FakeServer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TpsCommandTest {
    @Test
    public void onCommand() {
        FakeServer server = new FakeServer();
        FakeClient fakeClient = new FakeClient();
        TpsCommand tpsCommand = new TpsCommand(server);
        tpsCommand.onCommand("tps", new String[]{}, fakeClient);
        assertEquals(2, fakeClient.receivedMessages.size());
        fakeClient.reset();

        server.setTps(789, 15000L);
        tpsCommand.onCommand("tps", new String[]{}, fakeClient);
        assertEquals(2, fakeClient.receivedMessages.size());
        String tps = fakeClient.receivedMessages.get(0);
        String totalTps = fakeClient.receivedMessages.get(1);
        tps = tps.replaceAll("[^0-9]", "");
        totalTps = totalTps.replaceAll("[^0-9]", "");
        int intTps = Integer.parseInt(tps);
        long intTotalTps = Long.parseLong(totalTps);
        assertEquals(789, intTps);
        assertEquals(15000L, intTotalTps);
        fakeClient.reset();

        tpsCommand.onCommand("tps", new String[]{"help"}, fakeClient);
        assertTrue(fakeClient.sendMessageCalled);
        assertEquals(1, fakeClient.receivedMessages.size());
    }

}