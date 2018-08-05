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

package me.infuzion.chat.server.event;

import me.infuzion.chat.server.api.event.Event;
import me.infuzion.chat.server.api.event.IEventListener;
import me.infuzion.chat.server.api.event.IEventManager;
import me.infuzion.chat.server.api.event.reflection.EventHandler;
import me.infuzion.chat.server.api.event.reflection.EventPriority;
import me.infuzion.chat.server.api.event.reflection.Listener;
import me.infuzion.chat.server.mock.FakeServer;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventManagerTest implements IEventListener {
    private boolean[] orderCalled = {false, false, false, false, false};

    @Test
    public void registerEvents() {
        orderCalled = new boolean[]{false, false, false, false, false};
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.fireEvent(new FakeEvent());
        eventManager.reset();
        assertTrue(orderCalled[0]);
    }

    @Test
    public void reset() {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.reset();
        eventManager.fireEvent(new FakeEvent());
        eventManager.reset();
    }

    @Test
    public void fireEventOrder() {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.fireEvent(new FakeEvent());
        eventManager.reset();
    }

    @Test
    public void invalidEventListener() {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerListener(new InvalidListener(), null);
        eventManager.fireEvent(new FakeEvent());
        for (Listener e : Event.getAllHandlers()) {
            if (e.getEventListener() instanceof InvalidListener) {
                fail("Should not be added");
            }
        }
    }

    @Test
    public void checkAllTestsFired() {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.fireEvent(new FakeEvent());
        for (boolean b : orderCalled) {
            assertTrue(b);
        }
        eventManager.reset();
    }

    @EventHandler // Defaults to normal
    public void onFakeEvent(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertFalse(orderCalled[1]);
        assertFalse(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[1] = true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFakeEventLow(FakeEvent event) {
        assertFalse(orderCalled[0]);
        assertFalse(orderCalled[1]);
        assertFalse(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[0] = true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFakeEventHigh(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertTrue(orderCalled[1]);
        assertFalse(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[2] = true;
    }

    @EventHandler(priority = EventPriority.VERY_HIGH)
    public void onFakeEventVeryHigh(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertTrue(orderCalled[1]);
        assertTrue(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[3] = true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFakeEventMonitor(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertTrue(orderCalled[1]);
        assertTrue(orderCalled[2]);
        assertTrue(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[4] = true;
    }


}