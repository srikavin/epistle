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
import me.infuzion.chat.server.api.event.reflection.EventHandler;

import static org.junit.jupiter.api.Assertions.fail;

public class InvalidListener implements IEventListener {
    @EventHandler
    public void invalid(Event a, boolean t) {
        fail("Should not be called");
    }

    public void invalid2(Object event) {
        fail("Should not be called");
    }


}
